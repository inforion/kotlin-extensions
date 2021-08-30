package ru.inforion.lab403.common.json.serializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import ru.inforion.lab403.common.identifier.Identifier
import ru.inforion.lab403.common.identifier.toIdentifier
import ru.inforion.lab403.common.json.deserialize
import ru.inforion.lab403.common.json.interfaces.JsonSerde
import ru.inforion.lab403.common.json.serialize
import java.lang.reflect.Type

object IdentifierSerde : JsonSerde<Identifier> {
    override fun serialize(src: Identifier, typeOfSrc: Type, context: JsonSerializationContext) =
        src.toString().serialize(context)

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
        json.deserialize<String>(context).toIdentifier()
}