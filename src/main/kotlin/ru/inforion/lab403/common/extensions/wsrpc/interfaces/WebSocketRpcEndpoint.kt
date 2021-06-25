package ru.inforion.lab403.common.extensions.wsrpc.interfaces

import ru.inforion.lab403.common.extensions.wsrpc.annotations.WebSocketRpcMethod

interface WebSocketRpcEndpoint {
    val name: String

    @WebSocketRpcMethod(close = true)
    fun destroy() = Unit
}