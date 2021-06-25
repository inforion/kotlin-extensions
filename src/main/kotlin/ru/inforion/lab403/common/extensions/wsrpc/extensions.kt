package ru.inforion.lab403.common.extensions.wsrpc

import ru.inforion.lab403.common.extensions.wsrpc.interfaces.WebSocketRpcEndpoint

inline fun <reified T: WebSocketRpcEndpoint> WebSocketRpcServer.destroyAll() {
    endpoints.values.mapNotNull { it.endpoint as? T }.forEach { it.destroy() }
}