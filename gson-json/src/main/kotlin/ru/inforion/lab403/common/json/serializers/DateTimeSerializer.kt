package ru.inforion.lab403.common.json.serializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import org.joda.time.DateTime
import ru.inforion.lab403.common.json.interfaces.JsonSerde
import ru.inforion.lab403.common.json.deserialize
import ru.inforion.lab403.common.json.serialize
import java.lang.reflect.Type

object DateTimeSerializer : JsonSerde<DateTime> {
    override fun serialize(src: DateTime, typeOfSrc: Type, context: JsonSerializationContext) =
        src.millis.serialize(context)

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
        DateTime(json.deserialize<Long>(context))
}