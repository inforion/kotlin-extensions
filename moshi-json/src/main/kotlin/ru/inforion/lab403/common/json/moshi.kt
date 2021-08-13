@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.json

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import okio.*
import java.io.File
import java.io.InputStream
import java.io.OutputStream

typealias Json = Moshi

typealias JsonBuilder = Moshi.Builder

fun defaultJsonBuilder(): JsonBuilder = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())

fun JsonBuilder.create(): Json = build()

@PublishedApi
internal val mappers = Array(16) { defaultJsonBuilder().create() }

inline val json get() = mappers.random()

//interface JsonSerde<T> : JsonSerializer<T>, JsonDeserializer<T>
//
//fun <T : Any> GsonBuilder.registerTypeAdapter(kClass: KClass<T>, serializer: JsonSerializer<T>): GsonBuilder =
//    registerTypeAdapter(kClass.java, serializer)
//
//fun <T : Any> GsonBuilder.registerTypeAdapter(kClass: KClass<T>, deserializer: JsonDeserializer<T>): GsonBuilder =
//    registerTypeAdapter(kClass.java, deserializer)
//
//fun <T : Any> GsonBuilder.registerTypeAdapter(kClass: KClass<T>, serde: JsonSerde<T>): GsonBuilder =
//    registerTypeAdapter(kClass.java, serde)


inline fun <reified T : Any> polymorphicTypesFactory(
    classes: Collection<Class<out T>>,
    field: String = "type",
    selector: (Class<out T>) -> String = { it.simpleName }
): PolymorphicJsonAdapterFactory<T> = PolymorphicJsonAdapterFactory
    .of(T::class.java, field)
    .apply {
        classes.forEach { withSubtype(it, selector(it)) }
    }


// Objects encoding extensions


inline fun File.bufferedSink() = sink().buffer()

inline fun File.bufferedSource() = source().buffer()

inline fun OutputStream.bufferedSink() = sink().buffer()

inline fun InputStream.bufferedSource() = source().buffer()



inline fun <T> T.toJson(cls: Class<T>, mapper: Json = json): String = cls.adapter(mapper).toJson(this)

inline fun <T> T.toJson(cls: Class<T>, stream: OutputStream, mapper: Json = json) =
    cls.adapter(mapper).toJson(stream.bufferedSink(), this)

inline fun <reified T> T.toJson(cls: Class<T>, file: File, mapper: Json = json) =
    cls.adapter(mapper).toJson(file.bufferedSink(), this)



inline fun <reified T> T.toJson(mapper: Json = json): String = toJson(T::class.java, mapper)

inline fun <reified T> T.toJson(stream: OutputStream, mapper: Json = json) = toJson(T::class.java, stream, mapper)

inline fun <reified T> T.toJson(file: File, mapper: Json = json) = toJson(T::class.java, file, mapper)


//inline fun <reified T> T.serialize(context: JsonSerializationContext): JsonElement =
//    context.serialize(this, T::class.java)

// Objects decoding extensions

inline fun <T> String.fromJsonOrNull(cls: Class<T>, mapper: Json = json): T? = cls.adapter(mapper).fromJson(this)

inline fun <T> InputStream.fromJsonOrNull(cls: Class<T>, mapper: Json = json): T? =
    cls.adapter(mapper).fromJson(bufferedSource())

inline fun <T> File.fromJsonOrNull(cls: Class<T>, mapper: Json = json): T? =
    cls.adapter(mapper).fromJson(bufferedSource())



inline fun <T> String.fromJson(cls: Class<T>, mapper: Json = json): T =
    fromJsonOrNull(cls, mapper) ?: throw NullPointerException("JSON string decode result is null")

inline fun <T> InputStream.fromJson(cls: Class<T>, mapper: Json = json): T =
    fromJsonOrNull(cls, mapper) ?: throw NullPointerException("JSON input stream decode result is null")

inline fun <T> File.fromJson(cls: Class<T>, mapper: Json = json): T =
    fromJsonOrNull(cls, mapper) ?: throw NullPointerException("JSON file decode result is null")



inline fun <reified T> String.fromJson(mapper: Json = json): T = fromJson(T::class.java, mapper)

inline fun <reified T> InputStream.fromJson(mapper: Json = json): T = fromJson(T::class.java, mapper)

inline fun <reified T> File.fromJson(mapper: Json = json): T = fromJson(T::class.java, mapper)


//inline fun <T> JsonElement.deserialize(cls: Class<T>, context: JsonDeserializationContext): T =
//    context.deserialize(this, cls)
//
//inline fun <reified T> JsonElement.deserialize(context: JsonDeserializationContext): T =
//    deserialize(T::class.java, context)


// Other json helpers

@PublishedApi
internal val commentsRegex = Regex("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)")

inline fun String.removeJsonComments() = replace(commentsRegex, " ")

inline fun InputStream.removeJsonComments() = bufferedReader().readText().removeJsonComments()