@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.json.dontimport

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ru.inforion.lab403.common.extensions.either
import ru.inforion.lab403.common.extensions.ifNotNull
import ru.inforion.lab403.common.json.annotations.JsonPolymorphicType
import ru.inforion.lab403.common.json.deserialize
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

inline val <T> Class<out T>.identifierOrName: String get() =
    annotations
        .filterIsInstance<JsonPolymorphicType>()
        .firstOrNull() ifNotNull { name } either { simpleName }


@PublishedApi internal inline val <T> Class<T>.token get() = object : TypeToken<T>() { }

@PublishedApi internal inline val <T> Class<T>.type: Type get() = token.type

@PublishedApi internal inline val <T> Class<T>.isGeneric: Boolean get() = typeParameters.isNotEmpty()

@PublishedApi internal inline val <T> Class<T>.classOrType: Type get() = if (isGeneric) token.type else this



internal inline fun Type.getParameterOrNull(index: Int) = if (this is ParameterizedType) {
    require(actualTypeArguments.size > index) { "Index of parameter out of bound" }
    actualTypeArguments[index]
} else null


// workaround to check type as JsonElement
internal inline val Type.isJsonElement get() = typeName.contains("JsonElement")


internal inline fun JsonPrimitive.parse(): Any = when {
    isBoolean -> asBoolean
    isNumber -> with(asString) {
        toIntOrNull() ?: toLongOrNull() ?: toFloatOrNull() ?: toDoubleOrNull()
        ?: error("Can't deserialize json primitive as number: $this")
    }
    isString -> asString
    else -> error("Can't deserialize json primitive: $this")
}

internal inline fun JsonElement.parse(context: JsonDeserializationContext): Any? = when {
    isJsonNull -> null
    isJsonObject -> deserialize<Map<String, *>>(context)
    isJsonArray -> deserialize<List<*>>(context)
    isJsonPrimitive -> asJsonPrimitive.parse()
    else -> error("Can't parse json element: $this")
}