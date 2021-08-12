package ru.inforion.lab403.common.json

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.joda.time.DateTime
import java.lang.reflect.Type
import kotlin.reflect.KType


@PublishedApi internal inline val <T> Class<T>.token get() = object : TypeToken<T>() {

}

@PublishedApi internal inline val <T> Class<T>.type: Type get() = token.type

@PublishedApi internal inline val <T> Class<T>.isGeneric: Boolean get() = typeParameters.isNotEmpty()

@PublishedApi internal inline  val <T> Class<T>.classOrTokenType get() = if (isGeneric) token.type else this


fun GsonBuilder.registerBasicClasses() = apply {
    registerTypeAdapter(ULong::class, ULongSerializer)
    registerTypeAdapter(UInt::class, UIntSerializer)
    registerTypeAdapter(UShort::class, UShortSerializer)
    registerTypeAdapter(UByte::class, UByteSerializer)

    registerTypeAdapter(KType::class, KTypeSerializer)
    registerTypeAdapter(DateTime::class, DateTimeSerializer)
}
