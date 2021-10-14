@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.kafka

import org.apache.kafka.clients.admin.*
import org.apache.kafka.clients.admin.OffsetSpec
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.ConsumerGroupState
import org.apache.kafka.common.KafkaFuture
import org.apache.kafka.common.Node
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.errors.TopicExistsException
import ru.inforion.lab403.common.extensions.*
import ru.inforion.lab403.common.logging.logger
import java.io.Closeable
import java.util.concurrent.ExecutionException


class KafkaAdminTool constructor(val brokers: String, val timeout: Long) : Closeable {
    companion object {
        val log = logger()
    }

    private val client = AdminClient.create(
        mapOf(
            AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to brokers
        )
    )

    fun nodes(): Collection<Node> = client.describeCluster().nodes().getOrThrow(timeout)

    fun clusterId(): String = client.describeCluster().clusterId().getOrThrow(timeout)

    fun check(): Boolean = client.describeCluster().clusterId().getOrNull(timeout) != null

    fun listTopics(): Set<String> = client.listTopics().names().getOrThrow(timeout)

    fun deleteTopics(topics: Collection<String>) = client.deleteTopics(topics).getOrThrow(timeout)

    fun createTopics(topics: Collection<NewTopic>) = client.createTopics(topics).getOrThrow(timeout)

    fun describeTopics(topics: Collection<String>): Collection<TopicDescription> = client.describeTopics(topics).getOrThrow(timeout).values

    fun describeGroups(groups: Collection<String>): Map<String, KafkaFuture<ConsumerGroupDescription>> =
        client.describeConsumerGroups(groups).describedGroups()

    fun listOffsets(offsets: Collection<TopicPartition>, offsetSpec: OffsetSpec): Map<TopicPartition, Long> {
        val parameters = offsets.associateWith { offsetSpec }
        val result = client.listOffsets(parameters).getOrThrow(timeout)
        return result.associate { it.key to it.value.offset() }
    }

    fun listConsumerGroupOffsets(group: String): Map<TopicPartition, Long> =
        client.listConsumerGroupOffsets(group)
            .partitionsToOffsetAndMetadata()
            .getOrThrow(timeout)
            .associate {
                it.key to it.value.offset()
            }

    fun topicsInfo(topics: Collection<String>) = describeTopics(topics).map { topic ->
        val parameters = topic.toTopicPartitions()
        val partitions = listOffsets(parameters, OffsetSpec.latest()).map { (partition, offset) ->
            PartitionInfo(partition.partition(), -1, offset)
        }
        TopicInfo(topic.name(), partitions)
    }

    fun consumersInfo(groups: Collection<String>) = groups.associateWith { group ->
        val offsets = listConsumerGroupOffsets(group)

        // assume group for single topic
        offsets.keys.firstOrNull() ifNotNull {
            val partitions = listOffsets(offsets.keys, OffsetSpec.latest()).map { (partition, size) ->
                val offset = offsets.getValue(partition)
                PartitionInfo(partition.partition(), offset, size)
            }

            TopicInfo(topic(), partitions)
        }
    }

    fun createAndConfigureTopic(topic: Topic, retries: Int = 10, delay: Long = 100) {
        val newTopic = NewTopic(topic.name, topic.partitions, topic.replication).configs(topic.config())

        // kafka returns from get() method of deleteTopics() but
        // when try to createTopics() it fails with TopicExistsException exception
        repeat(retries) {
            try {
                createTopics(setOf(newTopic))
                return
            } catch (error: ExecutionException) {
                if (error.cause !is TopicExistsException) throw error
                Thread.sleep(delay)
            }
        }

        error("Can't create topic '$topic' within $retries retries because topic already exists")
    }

    fun resetOffsets(group: String, topic: Topic, retries: Int = 10) {
        describeGroups(setOf(group)).map { (group, desc) ->
            var counter = retries

            while (counter > 0) {
                val state = desc.getOrThrow(timeout).state()
                if (state == ConsumerGroupState.DEAD || state == ConsumerGroupState.EMPTY) break
                else counter -= 1
            }

            val state = desc.getOrThrow(timeout).state()

            require(state == ConsumerGroupState.EMPTY || state == ConsumerGroupState.DEAD) {
                "Assignments can only be reset if the group '$group' is inactive, but the current state is $state"
            }

            deleteTopics(setOf(topic.name))
            createAndConfigureTopic(topic, retries)

            val partitionsToReset = describeTopics(setOf(topic.name)).flatMap { it.toTopicPartitions() }
            val preparedOffsets = prepareOffsetsToReset(partitionsToReset, OffsetSpec.earliest())
            client.alterConsumerGroupOffsets(group, preparedOffsets)
        }.forEach {
            it.getOrThrow(timeout)
        }
    }

    private fun prepareOffsetsToReset(partitions: Collection<TopicPartition>, offsetSpec: OffsetSpec): Map<TopicPartition, OffsetAndMetadata> {
        val logStartOffsets = listOffsets(partitions, offsetSpec)
        return partitions.associateWith {
            val offset = logStartOffsets[it]
            require(offset != null) { "Error getting starting offset of topic partition: $it" }
            OffsetAndMetadata(offset)
        }
    }

    /**
     * Returns true if kafka is available and all topics have been read by consumer, otherwise returns false
     */
    fun checkAllDataConsumed(groups: Collection<String>) = with(consumersInfo(groups)) {
        all { (group, info) ->
            requireNotNull(info) { "Topic for group $group not exists" }
                .partitions.all { it.size == it.offset }
        }
    }

    override fun close() = client.close()
}