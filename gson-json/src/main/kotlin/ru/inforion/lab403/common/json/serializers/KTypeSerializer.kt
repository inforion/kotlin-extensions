package ru.inforion.lab403.common.json.serializers

import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import ru.inforion.lab403.common.json.serialize
import java.lang.reflect.Type
import kotlin.reflect.KType

object KTypeSerializer : JsonSerializer<KType> {
    override fun serialize(src: KType, typeOfSrc: Type, context: JsonSerializationContext) =
        src::class.qualifiedName.serialize(context)
}