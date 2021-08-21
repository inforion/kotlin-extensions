package ru.inforion.lab403.common.json

import org.joda.time.DateTime
import ru.inforion.lab403.common.json.serializers.*
import kotlin.reflect.KType

@PublishedApi
internal val mappers = Array(16) { defaultJsonBuilder().create() }

inline val json: Json get() = mappers.random()

fun JsonBuilder.registerBasicClasses() = apply {
    registerTypeAdapter(Map::class, MapDeserializer)
    registerTypeAdapter(List::class, ListDeserializer)

    registerTypeAdapter(ULong::class, ULongSerde)
    registerTypeAdapter(UInt::class, UIntSerde)
    registerTypeAdapter(UShort::class, UShortSerde)
    registerTypeAdapter(UByte::class, UByteSerde)

    registerTypeAdapter(KType::class, KTypeSerializer)
    registerTypeAdapter(DateTime::class, DateTimeSerde)
}

fun defaultJsonBuilder(): JsonBuilder = JsonBuilder()
    .serializeNulls()
    .registerBasicClasses()