@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types


@PublishedApi internal inline fun <T> Class<T>.adapter(mapper: Json): JsonAdapter<T> {
    return if (typeParameters.isNotEmpty()) {
        val type = Types.newParameterizedType(this, *typeParameters)
        mapper.adapter(type)
    } else {
        mapper.adapter(this)
    }
}


//fun JsonBuilder.registerBasicClasses() = apply {
//    registerTypeAdapter(ULong::class, ULongSerializer)
//    registerTypeAdapter(UInt::class, UIntSerializer)
//    registerTypeAdapter(UShort::class, UShortSerializer)
//    registerTypeAdapter(UByte::class, UByteSerializer)
//
//    registerTypeAdapter(KType::class, KTypeSerializer)
//    registerTypeAdapter(DateTime::class, DateTimeSerializer)
//}
