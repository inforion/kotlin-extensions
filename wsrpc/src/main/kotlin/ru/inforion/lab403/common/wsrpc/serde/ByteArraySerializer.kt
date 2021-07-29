package ru.inforion.lab403.common.wsrpc.serde

import com.google.gson.*
import ru.inforion.lab403.common.extensions.b64decode
import ru.inforion.lab403.common.extensions.b64encode
import ru.inforion.lab403.common.json.JsonSerde
import java.lang.reflect.Type

object ByteArraySerializer : JsonSerde<ByteArray> {
    internal data class ByteArrayDescriptor(val __bytes__: String)

    override fun serialize(src: ByteArray, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val descriptor = ByteArrayDescriptor(src.b64encode())
        return context.serialize(descriptor)
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ByteArray {
        val descriptor = context.deserialize<ByteArrayDescriptor>(json, typeOfT)
        return descriptor.__bytes__.b64decode()
    }
}
