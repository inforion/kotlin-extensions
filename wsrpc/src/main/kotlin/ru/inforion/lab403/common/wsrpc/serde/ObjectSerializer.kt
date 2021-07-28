package ru.inforion.lab403.common.wsrpc.serde

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.inforion.lab403.common.extensions.sure
import ru.inforion.lab403.common.json.encodeValue
import ru.inforion.lab403.common.wsrpc.WebSocketRpcServer
import ru.inforion.lab403.common.wsrpc.interfaces.WebSocketRpcEndpoint
import java.util.*
import kotlin.reflect.KClass

internal class ObjectSerializer<T: Any>(
    val server: WebSocketRpcServer,
    kClass: KClass<T>,
    val apiGen: (T) -> WebSocketRpcEndpoint
) : KSerializer<T> {
    data class ObjectDescription(val __rpc__: String, val endpoint: UUID)

    val name = kClass.simpleName.sure { "Can't create endpoint for anonymous: $kClass" }

    override fun deserialize(decoder: Decoder): T = throw NotImplementedError("Endpoint object not deserializable")

    override val descriptor = PrimitiveSerialDescriptor("Object", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: T) {
        val holder = server.register(apiGen(value))
        encoder.encodeValue(ObjectDescription(name, holder.uuid))
    }
}