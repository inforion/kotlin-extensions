@file:Suppress("unused", "NOTHING_TO_INLINE", "UNCHECKED_CAST")

package ru.inforion.lab403.common.json

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.serializer
import java.io.File
import java.io.InputStream
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType

// Decoder extensions

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

inline fun <reified T> String.fromJson(mapper: Json = Json): T =
    mapper.decodeFromString(this)

inline fun <reified T> InputStream.fromJson(mapper: Json = json): T = bufferedReader().readText().fromJson(mapper)

inline fun <reified T> File.fromJson(mapper: Json = json): T = inputStream().fromJson(mapper)


inline fun String.fromJson(mapper: Json = json, type: KType) =
    mapper.decodeFromString(this, type)

inline fun <T : Any> String.fromJson(mapper: Json = json, kClass: KClass<T>) =
    mapper.decodeFromString(this, kClass)

//     from JsonElement

inline fun <reified T> JsonElement.fromJson(mapper: Json = json): T =
    mapper.decodeFromJsonElement(this)

inline fun JsonElement.fromJson(mapper: Json = json, type: KType) =
    mapper.decodeFromJsonElement(this, type)