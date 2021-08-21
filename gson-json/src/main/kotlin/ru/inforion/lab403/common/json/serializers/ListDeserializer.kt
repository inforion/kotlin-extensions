package ru.inforion.lab403.common.json.serializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ru.inforion.lab403.common.json.dontimport.deserialize
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

object ListDeserializer : JsonDeserializer<List<*>> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): List<*> {
        val typeOfE = if (typeOfT is ParameterizedType) {
            require(typeOfT.actualTypeArguments.size == 1) { "Parametrized list must have exactly 1 parameter" }
             typeOfT.actualTypeArguments[0]
        } else null
        return  json.asJsonArray.deserialize(context, typeOfE)
    }
}