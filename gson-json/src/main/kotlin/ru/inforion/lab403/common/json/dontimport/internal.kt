package ru.inforion.lab403.common.json.dontimport

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ru.inforion.lab403.common.extensions.either
import ru.inforion.lab403.common.extensions.ifNotNull
import ru.inforion.lab403.common.json.annotations.Identifier
import ru.inforion.lab403.common.json.deserialize
import java.lang.reflect.Type

inline val <T> Class<out T>.identifierOrName: String get() =
    annotations.filterIsInstance<Identifier>().firstOrNull() ifNotNull { name } either { simpleName }



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

internal fun JsonArray.deserialize(context: JsonDeserializationContext, typeOfE: Type?) =
    map { it.deserialize(context, typeOfE) }

internal fun JsonObject.deserialize(context: JsonDeserializationContext, typeOfV: Type?) =
    entrySet().associate { it.key to it.value.deserialize(context, typeOfV) }

internal fun JsonElement.deserialize(context: JsonDeserializationContext, type: Type?): Any? = when {
    "JsonElement" in type!!.typeName  -> this
    isJsonNull -> null
    isJsonObject -> when(type) {
        null -> deserialize<Map<String, *>>(context)
        else -> context.deserialize<Any?>(this, type)
    }
    isJsonArray -> deserialize<List<*>>(context)
    isJsonPrimitive -> (this as JsonPrimitive).deserialize()
    else -> error("Can't parse json element: $this")
}