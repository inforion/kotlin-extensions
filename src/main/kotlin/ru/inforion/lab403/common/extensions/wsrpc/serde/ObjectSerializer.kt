package ru.inforion.lab403.common.extensions.wsrpc.serde

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import ru.inforion.lab403.common.extensions.sure
import ru.inforion.lab403.common.extensions.wsrpc.WebSocketRpcServer
import ru.inforion.lab403.common.extensions.wsrpc.interfaces.WebSocketRpcEndpoint
import java.util.*
import kotlin.reflect.KClass

internal class ObjectSerializer<T: Any>(
    val server: WebSocketRpcServer,
    kClass: KClass<T>,
    val apiGen: (T) -> WebSocketRpcEndpoint
) : JsonSerializer<T>() {
    data class ObjectDescription(val __rpc__: String, val endpoint: UUID)

    val name = kClass.simpleName.sure { "Can't create endpoint for anonymous: $kClass" }

    override fun serialize(value: T, gen: JsonGenerator, serializers: SerializerProvider) {
        val holder = server.register(apiGen(value))
        gen.writeObject(ObjectDescription(name, holder.uuid))
    }
}