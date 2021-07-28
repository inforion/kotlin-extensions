@file:Suppress("unused", "NOTHING_TO_INLINE", "UNCHECKED_CAST")

package ru.inforion.lab403.common.json

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.serializer
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType


@PublishedApi internal val jsons = Array(16) {
    Json {
        isLenient = true
        ignoreUnknownKeys = true
    }
}

inline val json get() = jsons.random()

// Encoder and decoder extensions

inline fun <reified T> Encoder.encodeValue(value: T) =
    encodeSerializableValue(serializersModule.serializer(), value)

inline fun <reified T> Decoder.decodeValue(): T =
    decodeSerializableValue(serializersModule.serializer())

// Json decoding extensions

inline fun Json.decodeFromString(value: String, type: KType) =
    decodeFromString(serializersModule.serializer(type), value)

inline fun <T : Any> Json.decodeFromString(value: String, kClass: KClass<T>) =
    decodeFromString(serializersModule.serializer(kClass.starProjectedType), value) as T

inline fun <reified T>Json.decodeFromJsonElement(value: JsonElement): T =
    decodeFromJsonElement(serializersModule.serializer(), value)

inline fun Json.decodeFromJsonElement(value: JsonElement, type: KType) =
    decodeFromJsonElement(serializersModule.serializer(type), value)

inline fun <T : Any> Json.decodeFromJsonElement(value: JsonElement, kClass: KClass<T>) =
    decodeFromJsonElement(serializersModule.serializer(kClass.starProjectedType), value)

// Objects decoding extensions

//     from String

inline fun <reified T> String.parseJson(mapper: Json = Json): T =
    mapper.decodeFromString(this)

inline fun <reified T> InputStream.parseJson(mapper: Json = json): T = bufferedReader().readText().parseJson(mapper)

inline fun <reified T> File.parseJson(mapper: Json = json): T = inputStream().parseJson(mapper)


inline fun String.parseJson(mapper: Json = json, type: KType) =
    mapper.decodeFromString(this, type)

inline fun <T : Any> String.parseJson(mapper: Json = json, kClass: KClass<T>) =
    mapper.decodeFromString(this, kClass)

//     from JsonElement

inline fun <reified T> JsonElement.parseJson(mapper: Json = json): T =
    mapper.decodeFromJsonElement(this)

inline fun JsonElement.parseJson(mapper: Json = json, type: KType) =
    mapper.decodeFromJsonElement(this, type)

// Json encoding extensions

inline fun Json.encodeToString(value: Any?, type: KType) =
    encodeToString(serializersModule.serializer(type), value)

inline fun <T: Any> Json.encodeToString(value: T, kClass: KClass<T>) =
    encodeToString(serializersModule.serializer(kClass.starProjectedType), value)

inline fun Json.encodeToJsonElement(value: Any?, type: KType) =
    encodeToJsonElement(serializersModule.serializer(type), value)

// Objects encoding extensions

inline fun <reified T> T.writeJson(mapper: Json = json) = mapper.encodeToString(this)

inline fun <reified T> T.writeJson(stream: OutputStream, mapper: Json = json) {
    val json = writeJson(mapper)
    with (stream.bufferedWriter()) { write(json) }
}

inline fun <reified T> T.writeJson(file: File, mapper: Json = json) = writeJson(file.outputStream(), mapper)

inline fun Any?.writeJson(mapper: Json = json, type: KType) = mapper.encodeToString( this, type)

inline fun <T: Any> T.writeJson(mapper: Json = json, cls: KClass<T>) = mapper.encodeToString( this, cls)


//inline fun <reified T: Any> Map<String, Any?>.parseJson(m: ObjectMapper = jacksonMappers.random()): T = m.convertValue(this, T::class.java)
//
//inline fun <reified T: Any> Any.parseJsonAsMap(m: ObjectMapper = jacksonMappers.random()): T = (this as Map<String, Any?>).parseJson(m)
//
//inline fun <reified T: Any> Any.parseJsonAsString(m: ObjectMapper = jacksonMappers.random()): T = (this as String).parseJson(m)