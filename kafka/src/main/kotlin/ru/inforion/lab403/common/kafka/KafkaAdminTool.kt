@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.kafka

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.OffsetSpec
import org.apache.kafka.clients.admin.TopicDescription
import org.apache.kafka.common.Node
import org.apache.kafka.common.TopicPartition
import ru.inforion.lab403.common.extensions.associate
import ru.inforion.lab403.common.extensions.ifNotNull
import ru.inforion.lab403.common.logging.logger
import java.io.Closeable


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

    fun describeTopics(topics: Collection<String>): Collection<TopicDescription> =
        client.describeTopics(topics).all().getOrThrow(timeout).values

    fun listOffsets(offsets: Collection<TopicPartition>): Map<TopicPartition, Long> {
        val parameters = offsets.associateWith { OffsetSpec.latest() }
        val result = client.listOffsets(parameters).all().getOrThrow(timeout)
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
        val partitions = listOffsets(parameters).map { (partition, offset) ->
            PartitionInfo(partition.partition(), -1, offset)
        }
        TopicInfo(topic.name(), partitions)
    }

    fun consumersInfo(groups: Collection<String>) = groups.associateWith { group ->
        val offsets = listConsumerGroupOffsets(group)

        // assume group for single topic
        offsets.keys.firstOrNull() ifNotNull {
            val partitions = listOffsets(offsets.keys).map { (partition, size) ->
                val offset = offsets.getValue(partition)
                PartitionInfo(partition.partition(), offset, size)
            }

            TopicInfo(topic(), partitions)
        }
    }

    /**
     * Returns true if kafka is available and all topics have been read by consumer, otherwise returns false
     */
    fun kafkaIsAvailable(groups: Collection<String>) = with(consumersInfo(groups)) {
        !any { (_, topicInfo) ->
            topicInfo == null
        } and !any { (_, topicInfo) ->
            topicInfo!!.partitions.any { it.size != it.offset }
        }
    }

    override fun close() = client.close()
}