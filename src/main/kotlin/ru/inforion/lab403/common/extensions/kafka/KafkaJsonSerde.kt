@file:Suppress("unused")

package ru.inforion.lab403.common.extensions.kafka

import com.fasterxml.jackson.databind.Module
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer
import ru.inforion.lab403.common.extensions.convertToString
import ru.inforion.lab403.common.extensions.jsonParser

open class KafkaJsonSerde<T>(private val cls: Class<T>, vararg modules: Module) : Serializer<T>, Deserializer<T> {
    private val mapper = jsonParser(indent = false).apply {
        modules.forEach { registerModule(it) }
    }

    override fun serialize(topic: String, data: T): ByteArray = mapper.writeValueAsBytes(data)

    override fun deserialize(topic: String, data: ByteArray): T = mapper.readValue(data.convertToString(), cls)

    override fun close() = Unit

    override fun configure(configs: MutableMap<String, *>, isKey: Boolean) = Unit
}