package ru.inforion.lab403.common.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import ru.inforion.lab403.common.extensions.b64decode
import ru.inforion.lab403.common.extensions.b64encode
import java.lang.reflect.Type
import kotlin.reflect.KType

object ByteArraySerializer : JsonSerde<ByteArray> {
    internal data class ByteArrayDescriptor(val __bytes__: String)

    private fun ByteArray.toDescriptor() = ByteArrayDescriptor(b64encode())

    private fun ByteArrayDescriptor.toByteArray() = __bytes__.b64decode()

    override fun serialize(src: ByteArray, typeOfSrc: Type, context: JsonSerializationContext) =
        src.toDescriptor().serialize(context)

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
        json.deserialize<ByteArrayDescriptor>(context).toByteArray()
}

object UByteSerializer : JsonSerde<UByte> {
    override fun serialize(src: UByte, typeOfSrc: Type, context: JsonSerializationContext) =
        src.toByte().serialize(context)

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
        json.deserialize<Byte>(context).toUByte()
}

object UShortSerializer : JsonSerde<UShort> {
    override fun serialize(src: UShort, typeOfSrc: Type, context: JsonSerializationContext) =
        src.toShort().serialize(context)

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
        json.deserialize<Short>(context).toUShort()
}

object UIntSerializer : JsonSerde<UInt> {
    override fun serialize(src: UInt, typeOfSrc: Type, context: JsonSerializationContext) =
        src.toInt().serialize(context)

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
        json.deserialize<Int>(context).toUInt()
}

object ULongSerializer : JsonSerde<ULong> {
    override fun serialize(src: ULong, typeOfSrc: Type, context: JsonSerializationContext) =
        src.toLong().serialize(context)

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
        json.deserialize<Long>(context).toULong()
}

object KTypeSerializer : JsonSerializer<KType> {
    override fun serialize(src: KType, typeOfSrc: Type, context: JsonSerializationContext) =
        src::class.qualifiedName.serialize(context)
}