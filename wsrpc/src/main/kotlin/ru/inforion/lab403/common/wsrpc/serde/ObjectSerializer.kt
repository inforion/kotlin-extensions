package ru.inforion.lab403.common.wsrpc.serde

import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import ru.inforion.lab403.common.extensions.sure
import ru.inforion.lab403.common.wsrpc.WebSocketRpcServer
import ru.inforion.lab403.common.wsrpc.interfaces.WebSocketRpcEndpoint
import java.lang.reflect.Type
import java.util.*
import kotlin.reflect.KClass

internal class ObjectSerializer<T: Any>(
    val server: WebSocketRpcServer,
    kClass: KClass<T>,
    val apiGen: (T) -> WebSocketRpcEndpoint
) : JsonSerializer<T> {
    data class ObjectDescription(val __rpc__: String, val endpoint: UUID)

    private val name = kClass.simpleName.sure { "Can't create endpoint for anonymous: $kClass" }

    override fun serialize(src: T, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val holder = server.register(apiGen(src))
        return context.serialize(ObjectDescription(name, holder.uuid))
    }
}