@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.wsrpc.serde

import com.google.gson.*
import ru.inforion.lab403.common.extensions.b64decode
import ru.inforion.lab403.common.extensions.b64encode
import ru.inforion.lab403.common.json.JsonSerde
import ru.inforion.lab403.common.json.deserialize
import ru.inforion.lab403.common.json.serialize
import java.lang.reflect.Type

object ByteArraySerializer : JsonSerde<ByteArray> {
    internal data class ByteArrayDescriptor(val __bytes__: String)

    private inline fun ByteArray.toDescriptor() = ByteArrayDescriptor(b64encode())

    private inline fun ByteArrayDescriptor.toByteArray() = __bytes__.b64decode()

    override fun serialize(src: ByteArray, typeOfSrc: Type, context: JsonSerializationContext) =
        src.toDescriptor().serialize(context)

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
        json.deserialize<ByteArrayDescriptor>(context).toByteArray()
}
