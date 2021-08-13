package ru.inforion.lab403.common.json

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


@PublishedApi internal inline val <T> Class<T>.token get() = object : TypeToken<T>() {

}

@PublishedApi internal inline val <T> Class<T>.type: Type get() = token.type

@PublishedApi internal inline val <T> Class<T>.isGeneric: Boolean get() = typeParameters.isNotEmpty()

@PublishedApi internal inline  val <T> Class<T>.classOrType: Type get() = if (isGeneric) token.type else this
