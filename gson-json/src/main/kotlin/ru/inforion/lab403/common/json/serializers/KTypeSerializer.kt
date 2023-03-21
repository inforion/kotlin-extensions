package ru.inforion.lab403.common.json.serializers

import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import ru.inforion.lab403.common.json.serialize
import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter

object KTypeSerializer : JsonSerializer<KType> {
    override fun serialize(src: KType, typeOfSrc: Type, context: JsonSerializationContext) =
        when (val classifier = src.classifier) {
            is KClass<*> -> classifier.simpleName
            is KTypeParameter -> classifier.name
            else -> "Unit"
        }.serialize(context)
}