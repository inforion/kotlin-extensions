@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.kafka

import org.apache.kafka.clients.admin.*
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.KafkaFuture
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.config.TopicConfig
import org.apache.kafka.common.header.Headers
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer
import org.joda.time.DateTime
import java.time.Duration
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

inline val <K, V> ConsumerRecord<K, V>.value: V get() = value()

inline val <K, V> ConsumerRecord<K, V>.key: K get() = key()

inline val <K, V> ConsumerRecord<K, V>.headers: Headers get() = headers()

inline val <K, V> ConsumerRecord<K, V>.timestamp: Long get() = timestamp()

inline val <K, V> ConsumerRecord<K, V>.datetime: DateTime get() = DateTime(timestamp)

inline operator fun <K, V> ConsumerRecord<K, V>.component1() = key

inline operator fun <K, V> ConsumerRecord<K, V>.component2() = value

typealias KafkaProperties = Map<String, Any>

inline fun <K, V> kafkaConsumerProperties(
    servers: String,
    group: String,
    keySerializer: Class<out Deserializer<K>>,
    valueSerializer: Class<out Deserializer<V>>,
    autoOffsetReset: String = "latest",
    enableAutoCommit: Boolean = true
) = mapOf(
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to servers,
    ConsumerConfig.GROUP_ID_CONFIG to group,
    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to keySerializer,
    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to valueSerializer,
    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to autoOffsetReset,
    ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to enableAutoCommit
)

inline fun <K, V> kafkaProducerProperties(
    servers: String,
    keySerializer: Class<out Serializer<K>>,
    valueSerializer: Class<out Serializer<V>>,
) = mapOf(
    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to servers,
    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to keySerializer,
    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to valueSerializer
)

inline fun <K, V> kafkaConsumer(topic: String, properties: KafkaProperties) =
    KafkaConsumer<K, V>(properties).apply { subscribe(listOf(topic)) }

inline fun <K, V> kafkaProducer(properties: KafkaProperties) = KafkaProducer<K, V>(properties)

inline fun <K, V> KafkaConsumer<K, V>.consume(timeout: Duration) = sequence {
    while (true) poll(timeout).forEach { yield(it) }
}

inline fun <K, V> KafkaProducer<K, V>.send(topic: String, key: K, value: V): Future<RecordMetadata> =
    send(ProducerRecord(topic, key, value))

inline fun TopicDescription.toTopicPartitions() = partitions().map { TopicPartition(name(), it.partition()) }

@OptIn(ExperimentalStdlibApi::class)
inline fun Topic.config() = buildMap<String, String> {
    if (retention != null) put(TopicConfig.RETENTION_BYTES_CONFIG, retention.toString())
    if (segment != null) put(TopicConfig.SEGMENT_BYTES_CONFIG, segment.toString())
    if (policy != null) put(TopicConfig.CLEANUP_POLICY_CONFIG, policy)
}

inline fun <T> KafkaFuture<T>.getOrThrow(millis: Long = -1): T =
    if (millis < 0) get() else get(millis, TimeUnit.MILLISECONDS)

inline fun <T> KafkaFuture<T>.getOrNull(millis: Long = -1): T? =
    runCatching { getOrThrow(millis) }.getOrNull()

inline fun DeleteTopicsResult.getOrThrow(millis: Long = -1) = all().getOrThrow(millis)

inline fun DeleteConsumerGroupsResult.getOrThrow(millis: Long = -1) = all().getOrThrow(millis)

inline fun CreateTopicsResult.getOrThrow(millis: Long = -1) = all().getOrThrow(millis)

inline fun DescribeTopicsResult.getOrThrow(millis: Long = -1) = all().getOrThrow(millis)

inline fun ListOffsetsResult.getOrThrow(millis: Long = -1) = all().getOrThrow(millis)

inline fun AlterConsumerGroupOffsetsResult.getOrThrow(millis: Long = -1) = all().getOrThrow(millis)