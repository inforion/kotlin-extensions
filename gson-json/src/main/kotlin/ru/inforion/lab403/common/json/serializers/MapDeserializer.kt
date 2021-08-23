package ru.inforion.lab403.common.json.serializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ru.inforion.lab403.common.json.dontimport.getParameterOrNull
import ru.inforion.lab403.common.json.dontimport.isJsonElement
import ru.inforion.lab403.common.json.dontimport.parse
import java.lang.reflect.Type

object MapDeserializer : JsonDeserializer<Map<*, *>> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Map<*, *> {
        val parameterType = typeOfT.getParameterOrNull(1)
        val isJsonElement = parameterType?.isJsonElement ?: false
        return with (json.asJsonObject.entrySet()) {
            if (!isJsonElement) associate { it.key to it.value.parse(context, parameterType) } else associate { it.key to it.value }
        }
    }
}