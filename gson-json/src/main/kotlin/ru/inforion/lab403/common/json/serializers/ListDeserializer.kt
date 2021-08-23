package ru.inforion.lab403.common.json.serializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ru.inforion.lab403.common.json.dontimport.getParameterOrNull
import ru.inforion.lab403.common.json.dontimport.isJsonElement
import ru.inforion.lab403.common.json.dontimport.parse
import java.lang.reflect.Type

object ListDeserializer : JsonDeserializer<List<*>> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): List<*> {
        val parameterType = typeOfT.getParameterOrNull(0)
        val isJsonElement = parameterType?.isJsonElement ?: false
        return with (json.asJsonArray) {
            if (!isJsonElement) map { it.parse(context, parameterType) } else map { it }
        }
    }
}