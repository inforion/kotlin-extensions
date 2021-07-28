@file:Suppress("unused")

package ru.inforion.lab403.common.kafka

import kotlinx.serialization.json.Json
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer
import ru.inforion.lab403.common.extensions.bytes
import ru.inforion.lab403.common.extensions.string
import ru.inforion.lab403.common.json.parseJson
import ru.inforion.lab403.common.json.writeJson
import kotlin.reflect.KClass

open class KafkaJsonSerde<T : Any>(private val cls: KClass<T>, vararg modules: Module) : Serializer<T>, Deserializer<T> {
    private val mapper = Json {
//        serializersModule =
//        modules.forEach { registerModule(it) }
    }

    override fun serialize(topic: String, data: T): ByteArray = data.writeJson(mapper, cls).bytes

    override fun deserialize(topic: String, data: ByteArray): T = data.string.parseJson(mapper, cls)

    override fun close() = Unit

    override fun configure(configs: MutableMap<String, *>, isKey: Boolean) = Unit
}