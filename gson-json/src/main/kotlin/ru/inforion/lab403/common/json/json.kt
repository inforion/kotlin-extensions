@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.json

import com.google.gson.*
import com.google.gson.internal.bind.MapTypeAdapterFactory
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import kotlin.reflect.KClass

@PublishedApi internal val mappers = Array(16) {
    GsonBuilder()
//    .registerTypeAdapter(UInt::class.java, UIntAdapter())
        .create()
}

inline val gson: Gson get() = mappers.random()

interface JsonSerde<T> : JsonSerializer<T>, JsonDeserializer<T>

fun <T: Any> GsonBuilder.registerTypeAdapter(kClass: KClass<T>, serializer: JsonSerializer<T>): GsonBuilder =
    registerTypeAdapter(kClass.java, serializer)

fun <T: Any> GsonBuilder.registerTypeAdapter(kClass: KClass<T>, deserializer: JsonDeserializer<T>): GsonBuilder =
    registerTypeAdapter(kClass.java, deserializer)

fun <T: Any> GsonBuilder.registerTypeAdapter(kClass: KClass<T>, serde: JsonSerde<T>): GsonBuilder =
    registerTypeAdapter(kClass.java, serde)

// Objects encoding extensions

inline fun <T: Any?> T.toJson(mapper: Gson = gson): String = mapper.toJson(this)

inline fun <T: Any?> T.toJson(stream: OutputStream, mapper: Gson = gson) =
    stream.writer().use { mapper.toJson(this, it) }

inline fun <T: Any?> T.toJson(file: File, mapper: Gson = gson) = toJson(file.outputStream(), mapper)

// Objects decoding extensions

inline fun <T> String.fromJson(cls: Class<T>, mapper: Gson = gson): T = mapper.fromJson(this, cls)

inline fun <T> InputStream.fromJson(cls: Class<T>, mapper: Gson = gson): T = mapper.fromJson(reader(), cls)

inline fun <T> File.fromJson(cls: Class<T>, mapper: Gson = gson): T = mapper.fromJson(reader(), cls)

inline fun <reified T> String.fromJson(mapper: Gson = gson): T = fromJson(T::class.java, mapper)

inline fun <reified T> InputStream.fromJson(mapper: Gson = gson): T = fromJson(T::class.java, mapper)

inline fun <reified T> File.fromJson(mapper: Gson = gson): T = fromJson(T::class.java, mapper)