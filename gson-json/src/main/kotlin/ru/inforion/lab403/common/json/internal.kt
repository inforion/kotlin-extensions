package ru.inforion.lab403.common.json

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


@PublishedApi internal inline val <T> Class<T>.token get() = object : TypeToken<T>() {

}

@PublishedApi internal inline val <T> Class<T>.type: Type get() = token.type

@PublishedApi internal inline val <T> Class<T>.isGeneric: Boolean get() = typeParameters.isNotEmpty()

@PublishedApi internal inline val <T> Class<T>.classOrType: Type get() = if (isGeneric) token.type else this

internal fun JsonPrimitive.deserialize(): Any = when {
    isBoolean -> asBoolean
    isNumber -> with(asString) {
        toIntOrNull() ?: toLongOrNull() ?: toFloatOrNull() ?: toDoubleOrNull()
        ?: error("Can't deserialize json primitive as number: $this")
    }
    isString -> asString
    else -> error("Can't deserialize json primitive: $this")
}

internal fun JsonArray.deserialize(context: JsonDeserializationContext) = map { it.deserialize(context) }

internal fun JsonObject.deserialize(context: JsonDeserializationContext) =
    entrySet().associate { it.key to it.value.deserialize(context) }

internal fun JsonElement.deserialize(context: JsonDeserializationContext): Any? = when {
    isJsonNull -> null
    isJsonObject -> deserialize<Map<String, *>>(context)
    isJsonArray -> deserialize<List<*>>(context)
    isJsonPrimitive -> (this as JsonPrimitive).deserialize()
    else -> error("Can't parse json element: $this")
}