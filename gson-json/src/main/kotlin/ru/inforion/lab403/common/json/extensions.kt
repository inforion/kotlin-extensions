package ru.inforion.lab403.common.json

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import kotlin.reflect.KType


inline val <T> Class<T>.token get() = object : TypeToken<T>() {

}

inline val <T> Class<T>.type: Type get() = token.type

inline val <T> Class<T>.isGeneric: Boolean get() = typeParameters.isNotEmpty()


fun GsonBuilder.registerBasicClasses() = apply {
    registerTypeAdapter(ByteArray::class, ByteArraySerializer)

    registerTypeAdapter(ULong::class, ULongSerializer)
    registerTypeAdapter(UInt::class, UIntSerializer)
    registerTypeAdapter(UShort::class, UShortSerializer)
    registerTypeAdapter(UByte::class, UByteSerializer)

    registerTypeAdapter(KType::class, KTypeSerializer)
}
