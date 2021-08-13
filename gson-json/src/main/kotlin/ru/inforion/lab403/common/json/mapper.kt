package ru.inforion.lab403.common.json

import org.joda.time.DateTime
import ru.inforion.lab403.common.json.serializers.*
import kotlin.reflect.KType

@PublishedApi
internal val mappers = Array(16) { defaultJsonBuilder().create() }

inline val json: Json get() = mappers.random()

fun JsonBuilder.registerBasicClasses() = apply {
    registerTypeAdapter(ULong::class, ULongSerializer)
    registerTypeAdapter(UInt::class, UIntSerializer)
    registerTypeAdapter(UShort::class, UShortSerializer)
    registerTypeAdapter(UByte::class, UByteSerializer)

    registerTypeAdapter(KType::class, KTypeSerializer)
    registerTypeAdapter(DateTime::class, DateTimeSerializer)
}

fun defaultJsonBuilder(): JsonBuilder = JsonBuilder()
    .serializeNulls()
    .registerBasicClasses()