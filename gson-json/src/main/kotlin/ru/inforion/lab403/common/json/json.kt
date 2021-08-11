@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.json

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.lang.reflect.Type
import kotlin.reflect.KClass

fun defaultGsonBuilder(): GsonBuilder = GsonBuilder().serializeNulls()

@PublishedApi
internal val mappers = Array(16) { defaultGsonBuilder().create() }

inline val gson: Gson get() = mappers.random()


inline val <T> Class<T>.token
    get() = object : TypeToken<T>() {

    }

inline val <T> Class<T>.type: Type get() = token.type


interface JsonSerde<T> : JsonSerializer<T>, JsonDeserializer<T>

fun <T : Any> GsonBuilder.registerTypeAdapter(kClass: KClass<T>, serializer: JsonSerializer<T>): GsonBuilder =
    registerTypeAdapter(kClass.java, serializer)

fun <T : Any> GsonBuilder.registerTypeAdapter(kClass: KClass<T>, deserializer: JsonDeserializer<T>): GsonBuilder =
    registerTypeAdapter(kClass.java, deserializer)

fun <T : Any> GsonBuilder.registerTypeAdapter(kClass: KClass<T>, serde: JsonSerde<T>): GsonBuilder =
    registerTypeAdapter(kClass.java, serde)


inline fun <T : Any> polymorphicTypesAdapter(
    classes: Collection<Class<out T>>,
    field: String = "type",
    selector: (Class<out T>) -> String = { it.simpleName }
): JsonSerde<T> {
    val name2cls = classes.associateBy(selector)
    val cls2name = classes.associateWith(selector)

    return object : JsonSerde<T> {
        override fun serialize(src: T, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            val type = cls2name[src::class.java]
            requireNotNull(type) { "Can't serialize object $src -> unknown name for class '${src::class.java}" }
            return (context.serialize(src) as JsonObject).apply {
                val property = get(field)
                require(property != null) { "Type field with name '$field' must exist" }
                require(property.isJsonNull) { "Type field with name '$field' must be null after serialization" }
                addProperty(field, type)
            }
        }

        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): T {
            val obj = json as JsonObject
            val type = obj[field] ?: throw IllegalArgumentException("No such field in Json object '$field")
            val cls = name2cls[type.asString]
            requireNotNull(cls) { "Unknown type name: '$type' for field: '$field'" }
            return json.deserialize(cls, context)
        }
    }
}


inline fun <reified T : Any> polymorphicTypesFactory(
    classes: Collection<Class<out T>>,
    field: String = "type",
    selector: (Class<out T>) -> String = { it.simpleName }
): RuntimeTypeAdapterFactory<T> = RuntimeTypeAdapterFactory
    .of(T::class.java, field)
    .apply {
        classes.forEach { registerSubtype(it, selector(it)) }
    }


// Objects encoding extensions

inline fun <T> T.toJson(cls: Class<T>, mapper: Gson = gson): String = mapper.toJson(this, cls.token.type)

inline fun <T> T.toJson(cls: Class<T>, stream: OutputStream, mapper: Gson = gson): Unit =
    stream.writer().use { mapper.toJson(this, cls.token.type, it) }

inline fun <reified T> T.toJson(cls: Class<T>, file: File, mapper: Gson = gson): Unit =
    toJson(cls, file.outputStream(), mapper)


inline fun <reified T> T.toJson(mapper: Gson = gson): String = toJson(T::class.java, mapper)

inline fun <reified T> T.toJson(stream: OutputStream, mapper: Gson = gson) = toJson(T::class.java, stream, mapper)

inline fun <reified T> T.toJson(file: File, mapper: Gson = gson) = toJson(T::class.java, file, mapper)


inline fun <reified T> T.serialize(context: JsonSerializationContext): JsonElement =
    context.serialize(this, T::class.java)

// Objects decoding extensions

inline fun <T> String.fromJson(cls: Class<T>, mapper: Gson = gson) = mapper.fromJson<T>(this, cls.token.type)

inline fun <T> JsonElement.fromJson(cls: Class<T>, mapper: Gson = gson) = mapper.fromJson<T>(this, cls.token.type)

inline fun <T> InputStream.fromJson(cls: Class<T>, mapper: Gson = gson) = mapper.fromJson<T>(reader(), cls.token.type)

inline fun <T> File.fromJson(cls: Class<T>, mapper: Gson = gson) = mapper.fromJson<T>(reader(), cls.token.type)


inline fun <reified T> String.fromJson(mapper: Gson = gson): T = fromJson(T::class.java, mapper)

inline fun <reified T> JsonElement.fromJson(mapper: Gson = gson): T = fromJson(T::class.java, mapper)

inline fun <reified T> InputStream.fromJson(mapper: Gson = gson): T = fromJson(T::class.java, mapper)

inline fun <reified T> File.fromJson(mapper: Gson = gson): T = fromJson(T::class.java, mapper)


inline fun <T> JsonElement.deserialize(cls: Class<T>, context: JsonDeserializationContext): T =
    context.deserialize(this, cls)

inline fun <reified T> JsonElement.deserialize(context: JsonDeserializationContext): T =
    deserialize(T::class.java, context)


// Other json helpers

@PublishedApi
internal val commentsRegex = Regex("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)")

inline fun String.removeJsonComments() = replace(commentsRegex, " ")

inline fun InputStream.removeJsonComments() = bufferedReader().readText().removeJsonComments()