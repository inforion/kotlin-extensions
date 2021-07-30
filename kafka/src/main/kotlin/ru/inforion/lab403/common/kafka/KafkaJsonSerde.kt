@file:Suppress("unused")

package ru.inforion.lab403.common.kafka

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer
import ru.inforion.lab403.common.extensions.bytes
import ru.inforion.lab403.common.extensions.string
import ru.inforion.lab403.common.json.fromJson
import ru.inforion.lab403.common.json.toJson
import kotlin.reflect.KClass

class KafkaJsonSerde<T : Any>(
    private val cls: KClass<T>,
    private val mapper: Gson
) : Serializer<T>, Deserializer<T> {

    companion object {
        operator fun <T : Any> invoke(cls: KClass<T>, build: GsonBuilder.() -> Unit): KafkaJsonSerde<T> {
            val gson = GsonBuilder().apply { build() }.create()
            return KafkaJsonSerde(cls, gson)
        }
    }

    override fun serialize(topic: String, data: T): ByteArray = data.toJson(cls.java, mapper).bytes

    override fun deserialize(topic: String, data: ByteArray): T = data.string.fromJson(cls.java, mapper)

    override fun close() = Unit

    override fun configure(configs: MutableMap<String, *>, isKey: Boolean) = Unit
}