@file:Suppress("unused")

package ru.inforion.lab403.common.extensions.wsrpc.endpoints

import ru.inforion.lab403.common.extensions.associate
import ru.inforion.lab403.common.extensions.wsrpc.interfaces.WebSocketRpcEndpoint
import ru.inforion.lab403.common.extensions.wsrpc.WebSocketRpcServer
import ru.inforion.lab403.common.extensions.wsrpc.annotations.WebSocketRpcMethod
import java.util.*

class ServiceEndpoint(
    val server: WebSocketRpcServer,
    override val name: String = "Service"
) : WebSocketRpcEndpoint {

    @WebSocketRpcMethod
    fun endpoints() = server.endpoints.associate { it.key to it.value.identifier }

    @WebSocketRpcMethod
    fun clear() = server.endpoints.values
        .filter { it.endpoint != this }
        .forEach { server.unregister(it.uuid) }

    @WebSocketRpcMethod
    fun unregister(uuid: UUID) = server.unregister(uuid)

    @WebSocketRpcMethod
    fun address() = server.address

    @WebSocketRpcMethod
    fun port() = server.port
}