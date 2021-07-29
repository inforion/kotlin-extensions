@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.json

import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.io.File
import java.io.OutputStream
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType

// Encoder extensions

inline fun <reified T> Encoder.encodeValue(value: T) =
    encodeSerializableValue(serializersModule.serializer(), value)

// Json encoding extensions

inline fun Json.encodeToString(value: Any?, type: KType) =
    encodeToString(serializersModule.serializer(type), value)

inline fun <T: Any> Json.encodeToString(value: T, kClass: KClass<T>) =
    encodeToString(serializersModule.serializer(kClass.starProjectedType), value)

inline fun Json.encodeToJsonElement(value: Any?, type: KType) =
    encodeToJsonElement(serializersModule.serializer(type), value)

// Objects encoding extensions

inline fun <reified T> T.toJson(mapper: Json = json) = mapper.encodeToString(this)

inline fun <reified T> T.toJson(stream: OutputStream, mapper: Json = json) {
    val json = toJson(mapper)
    with (stream.bufferedWriter()) { write(json) }
}

inline fun <reified T> T.toJson(file: File, mapper: Json = json) = toJson(file.outputStream(), mapper)

inline fun Any?.toJson(mapper: Json = json, type: KType) = mapper.encodeToString( this, type)

inline fun <T: Any> T.toJson(mapper: Json = json, cls: KClass<T>) = mapper.encodeToString( this, cls)