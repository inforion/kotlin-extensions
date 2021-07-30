@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.wsrpc.serde

import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import ru.inforion.lab403.common.extensions.sure
import ru.inforion.lab403.common.json.serialize
import ru.inforion.lab403.common.wsrpc.WebSocketRpcServer
import ru.inforion.lab403.common.wsrpc.interfaces.WebSocketRpcEndpoint
import java.lang.reflect.Type
import java.util.*
import kotlin.reflect.KClass

internal class ObjectSerializer<T> private constructor(
    val server: WebSocketRpcServer,
    val name: String,
    val apiGen: (T) -> WebSocketRpcEndpoint
) : JsonSerializer<T> {
    companion object {
        // Operator invoke used for curly brackets notation on object construction
        inline operator fun <T : Any> invoke(
            server: WebSocketRpcServer,
            kClass: KClass<T>,
            noinline apiGen: (T) -> WebSocketRpcEndpoint
        ) = invoke(server, kClass.simpleName.sure { "Can't create endpoint for: $kClass" }, apiGen)

        // Operator invoke used for curly brackets notation on object construction
        inline operator fun <reified T : Any> invoke(
            server: WebSocketRpcServer,
            noinline apiGen: (T) -> WebSocketRpcEndpoint
        ) = invoke(server, T::class, apiGen)

        // Operator invoke used for curly brackets notation on object construction
        inline operator fun <T> invoke(
            server: WebSocketRpcServer,
            name: String,
            noinline apiGen: (T) -> WebSocketRpcEndpoint
        ) = ObjectSerializer(server, name, apiGen)
    }

    data class ObjectDescription(val __rpc__: String, val endpoint: UUID)

    override fun serialize(src: T, typeOfSrc: Type, context: JsonSerializationContext) =
        with(server.register(apiGen(src))) {
            ObjectDescription(name, uuid).serialize(context)
        }
}