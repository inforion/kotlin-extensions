package ru.inforion.lab403.common.json.serializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

object ObjectDeserializer : JsonDeserializer<Any> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Any {
        return with(json.asString) {
            toIntOrNull() ?: toLongOrNull() ?: toFloatOrNull() ?: toDoubleOrNull()
        } ?: context.deserialize(json, typeOfT)
    }
}