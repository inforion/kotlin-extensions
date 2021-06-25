package ru.inforion.lab403.common.extensions.wsrpc

import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newFixedThreadPoolContext
import org.java_websocket.WebSocket
import org.java_websocket.exceptions.WebsocketNotConnectedException
import org.java_websocket.framing.Framedata
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import ru.inforion.lab403.common.extensions.*
import ru.inforion.lab403.common.logging.FINER
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.extensions.wsrpc.descs.Request
import ru.inforion.lab403.common.extensions.wsrpc.descs.Response
import ru.inforion.lab403.common.extensions.wsrpc.endpoints.ServiceEndpoint
import ru.inforion.lab403.common.extensions.wsrpc.interfaces.WebSocketRpcEndpoint
import java.io.Closeable
import java.lang.reflect.InvocationTargetException
import java.net.InetSocketAddress
import java.util.*


@OptIn(ObsoleteCoroutinesApi::class)
class WebSocketRpcServer constructor(
    val host: String = "localhost",
    val port: Int,

    val pingTimeout: Int = 10, // in seconds
    val isReuseAddress: Boolean = true,
    val isTcpNoDelayActive: Boolean = false
): Closeable {
    companion object {
        val log = logger(FINER)
    }

    private val myEndpoints = dictionaryOf<UUID, EndpointHolder>()

    fun register(endpoint: WebSocketRpcEndpoint, uuid: UUID? = null): EndpointHolder {
        val actual = uuid ?: uuid()
        require(actual !in myEndpoints) { "Endpoint with $uuid already registered!" }
        return EndpointHolder(this, endpoint, actual).also {
            myEndpoints[actual] = it
            it.onRegister()
            log.finest { "Endpoint $it registered" }
        }
    }

    fun unregister(uuid: UUID) {
        myEndpoints.remove(uuid)
            .sure { "Endpoint with $uuid not registered!" }
            .also {
                it.onUnregister()
                log.finest { "Endpoint $it unregistered" }
            }
    }

    val address = InetSocketAddress(host, port)

    private val threads = newFixedThreadPoolContext(availableProcessors, "WebSocketRpcServerPool")

    private val server = object : WebSocketServer(address) {
        private val mapper = jsonParser(indent = false)

        override fun onOpen(conn: WebSocket, handshake: ClientHandshake) {
            log.config { "Client[port=${conn.remoteSocketAddress.port}] established connection" }
        }

        override fun onClose(conn: WebSocket?, code: Int, reason: String, remote: Boolean) {
            log.config { "Client[port=${conn?.remoteSocketAddress?.port}] closed connection code=$code reason='$reason' remote=$remote" }
        }

        override fun onMessage(conn: WebSocket, message: String) {
            launch(threads) {
                log.debug { "Client[port=${conn.remoteSocketAddress.port}] message=$message" }
                runCatching {
                    mapper.readValue<Request>(message)
                }.onFailure {
                    log.severe { it.stackTraceToString() }
                }.onSuccess { request ->
                    val response = try {
                        val endpoint = myEndpoints[request.endpoint].sure { "Endpoint ${request.endpoint} not registered!" }
                        val result = endpoint.execute(request.method, request.values)
                        Response(request.uuid, result, null)
                    } catch (error: Throwable) {
                        log.severe { error.stackTraceToString() }

                        // removes all invocation onion because they make no sense to analyse actual error
                        var result: Throwable = error
                        while (result is InvocationTargetException)
                            result = result.targetException

                        Response(request.uuid, null, result.toString())
                    }

                    conn.runCatching { send(mapper.writeValueAsBytes(response)) }
                        .onFailure { log.severe { "Error during send response: $it" } }
                }
            }
        }

        override fun onError(conn: WebSocket?, ex: Exception) = when (ex) {
            is WebsocketNotConnectedException -> {
                log.warning { "Client[port=${conn?.remoteSocketAddress?.port}] close connection abruptly: $ex" }
            }
            else -> log.severe {
                ex.stackTraceToString()
            }
        }

        override fun onWebsocketPong(conn: WebSocket, f: Framedata) {
            log.finest { "Client[port=${conn.remoteSocketAddress.port}] pong received" }
        }

        override fun onStart() {
            log.config { "WebSocket server started at $host:$port" }
        }

        init {
            connectionLostTimeout = pingTimeout
            isReuseAddr = isReuseAddress
            isTcpNoDelay = isTcpNoDelayActive
        }
    }

    fun start() = server.start()

    fun stop() = server.stop()

    override fun close() = stop()

    val endpoints: Map<UUID, EndpointHolder> get() = myEndpoints

    internal val resources = ResourceManager()

    init {
        register(ServiceEndpoint(this), "ffffffff-ffff-ffff-ffff-ffffffffffff".toUUID())
    }
}