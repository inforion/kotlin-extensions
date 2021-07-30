package ru.inforion.lab403.common.wsrpc.endpoints

import ru.inforion.lab403.common.wsrpc.WebSocketRpcServer
import ru.inforion.lab403.common.wsrpc.annotations.WebSocketRpcMethod
import ru.inforion.lab403.common.wsrpc.interfaces.WebSocketRpcEndpoint
import java.util.*

class ServiceEndpoint(
    private val server: WebSocketRpcServer,
    override val name: String = WebSocketRpcServer.SERVICE_ENDPOINT_NAME
) : WebSocketRpcEndpoint {

    @WebSocketRpcMethod
    fun endpoints() = server.associate { it.uuid to it.identifier }

    @WebSocketRpcMethod
    fun clear() = server
        .filter { it.endpoint != this }
        .forEach { unregister(it.uuid) }

    @WebSocketRpcMethod
    fun unregister(uuid: UUID) = server.unregister(uuid)

    @WebSocketRpcMethod
    fun address() = server.address

    @WebSocketRpcMethod
    fun port() = server.port
}