package ru.inforion.lab403.common.json.serializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import ru.inforion.lab403.common.json.interfaces.JsonSerde
import ru.inforion.lab403.common.json.deserialize
import ru.inforion.lab403.common.json.serialize
import java.lang.reflect.Type

object UShortSerializer : JsonSerde<UShort> {
    override fun serialize(src: UShort, typeOfSrc: Type, context: JsonSerializationContext) =
        src.toShort().serialize(context)

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
        json.deserialize<Short>(context).toUShort()
}