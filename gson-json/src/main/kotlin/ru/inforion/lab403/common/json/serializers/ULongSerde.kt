package ru.inforion.lab403.common.json.serializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import ru.inforion.lab403.common.json.interfaces.JsonSerde
import ru.inforion.lab403.common.json.deserialize
import ru.inforion.lab403.common.json.serialize
import java.lang.reflect.Type

object ULongSerde : JsonSerde<ULong> {
    override fun serialize(src: ULong, typeOfSrc: Type, context: JsonSerializationContext) =
        src.toLong().serialize(context)

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
        json.deserialize<Long>(context).toULong()
}