package ru.inforion.lab403.common.json.serializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ru.inforion.lab403.common.json.deserialize
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

object MapDeserializer : JsonDeserializer<Map<*, *>> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Map<*, *> {
        val typeOfV = if (typeOfT is ParameterizedType) {
            require(typeOfT.actualTypeArguments.size == 2) { "Parametrized Map must have exactly 2 parameters" }
             typeOfT.actualTypeArguments[1]
        } else null
        return json.asJsonObject.deserialize(context, typeOfV)
    }
}