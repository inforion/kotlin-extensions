package ru.inforion.lab403.common.wsrpc.serde

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import ru.inforion.lab403.common.json.JsonSerde
import java.lang.reflect.Type
import java.util.*

internal object UuidSerializer: JsonSerde<UUID> {
    override fun serialize(src: UUID, typeOfSrc: Type, context: JsonSerializationContext) =
        JsonPrimitive(src.toString())

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): UUID =
        UUID.fromString(json.asString)
}