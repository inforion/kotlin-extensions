package ru.inforion.lab403.common.wsrpc.serde

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.inforion.lab403.common.extensions.b64decode
import ru.inforion.lab403.common.extensions.b64encode
import ru.inforion.lab403.common.json.decodeValue
import ru.inforion.lab403.common.json.encodeValue

@Serializable
class ByteArrayDescriptor(val __bytes__: String) {
    object Serde : KSerializer<ByteArray> {
        override val descriptor = PrimitiveSerialDescriptor("ByteArray", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: ByteArray) {
            val descriptor = ByteArrayDescriptor(value.b64encode())
            encoder.encodeValue(descriptor)
        }

        override fun deserialize(decoder: Decoder): ByteArray {
            val descriptor = decoder.decodeValue<ByteArrayDescriptor>()
            return descriptor.__bytes__.b64decode()
        }
    }
}
