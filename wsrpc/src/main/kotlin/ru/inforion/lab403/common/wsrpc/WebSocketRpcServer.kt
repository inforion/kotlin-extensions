@file:Suppress("unused")

package ru.inforion.lab403.common.wsrpc

import org.java_websocket.WebSocket
import org.java_websocket.exceptions.InvalidDataException
import org.java_websocket.exceptions.WebsocketNotConnectedException
import org.java_websocket.framing.Framedata
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import ru.inforion.lab403.common.concurrent.launch
import ru.inforion.lab403.common.concurrent.newFixedThreadPoolDispatcher
import ru.inforion.lab403.common.extensions.availableProcessors
import ru.inforion.lab403.common.extensions.sure
import ru.inforion.lab403.common.json.*
import ru.inforion.lab403.common.logging.FINER
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.uuid.toUUID
import ru.inforion.lab403.common.uuid.uuid
import ru.inforion.lab403.common.wsrpc.descs.Request
import ru.inforion.lab403.common.wsrpc.descs.Response
import ru.inforion.lab403.common.wsrpc.endpoints.ServiceEndpoint
import ru.inforion.lab403.common.wsrpc.interfaces.WebSocketRpcEndpoint
import java.io.Closeable
import java.lang.reflect.InvocationTargetException
import java.net.InetSocketAddress
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.set


class WebSocketRpcServer constructor(
    val host: String = "localhost",
    val port: Int,

    types: WebSocketTypesRegistry = WebSocketTypesRegistry {  },
    packages: WebSocketPackageRegistry = WebSocketPackageRegistry {  },

    val pingTimeout: Int = 100, // in seconds
    val isReuseAddress: Boolean = true,
    val isTcpNoDelayActive: Boolean = true,
) : Iterable<EndpointHolder>, Closeable {
    companion object {
        const val SERVICE_ENDPOINT_NAME = "Service"
        val SERVICE_ENDPOINT_UUID = "ffffffff-ffff-ffff-ffff-ffffffffffff".toUUID()

        val log = logger(FINER)
    }

    private val myEndpoints = ConcurrentHashMap<UUID, EndpointHolder>()

    fun register(endpoint: WebSocketRpcEndpoint, uuid: UUID? = null): EndpointHolder {
        val actual = uuid ?: uuid()

        require(!myEndpoints.contains(actual)) { "Endpoint with $uuid already registered!" }

        return EndpointHolder(this, endpoint, actual).also {
            myEndpoints[actual] = it
            it.onRegister()
            log.finer { "Endpoint $it registered" }
        }
    }

    fun unregister(uuid: UUID) {
        require(uuid != SERVICE_ENDPOINT_UUID) { "Can't unregister service endpoint!" }

        myEndpoints.remove(uuid)
            .sure { "Endpoint with $uuid not registered!" }
            .also {
                it.onUnregister()
                log.finer { "Endpoint $it unregistered" }
            }
    }

    val address = InetSocketAddress(host, port)

    private val threads = newFixedThreadPoolDispatcher(availableProcessors)

    private val server = object : WebSocketServer(address) {
        private val mapper = defaultJsonBuilder().create()

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
                    message.fromJson<Request>(mapper)
                }.onFailure {
                    log.severe { it.stackTraceToString() }
                }.onSuccess { request ->
                    val response = try {
                        val endpoint = myEndpoints[request.endpoint]
                            .sure { "Endpoint ${request.endpoint} not registered!" }
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

                    conn.runCatching { send(response.toJson(mapper)) }
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

    override fun close() {
        stop()
        threads.close()
    }

    internal val resources = ResourceManager(this, types, packages)

    init {
        register(ServiceEndpoint(this), SERVICE_ENDPOINT_UUID)
    }

    override fun iterator() = myEndpoints.values.iterator()
}