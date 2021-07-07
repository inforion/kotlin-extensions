package ru.inforion.lab403.common.wsrpc.interfaces

import ru.inforion.lab403.common.wsrpc.annotations.WebSocketRpcMethod

interface WebSocketRpcEndpoint {
    val name: String

    @WebSocketRpcMethod(close = true)
    fun destroy() = Unit
}