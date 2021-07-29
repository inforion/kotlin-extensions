package ru.inforion.lab403.common.wsrpc

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.java_websocket.WebSocket
import org.java_websocket.exceptions.WebsocketNotConnectedException
import org.java_websocket.framing.Framedata
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import ru.inforion.lab403.common.concurrent.launch
import ru.inforion.lab403.common.concurrent.newFixedThreadPoolDispatcher
import ru.inforion.lab403.common.extensions.associate
import ru.inforion.lab403.common.extensions.availableProcessors
import ru.inforion.lab403.common.extensions.dictionaryOf
import ru.inforion.lab403.common.extensions.sure
import ru.inforion.lab403.common.json.fromJson
import ru.inforion.lab403.common.json.toJson
import ru.inforion.lab403.common.logging.FINER
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.uuid.toUUID
import ru.inforion.lab403.common.uuid.uuid
import ru.inforion.lab403.common.wsrpc.annotations.WebSocketRpcMethod
import ru.inforion.lab403.common.wsrpc.descs.Request
import ru.inforion.lab403.common.wsrpc.descs.Response
import ru.inforion.lab403.common.wsrpc.interfaces.WebSocketRpcEndpoint
import ru.inforion.lab403.common.wsrpc.serde.registerBasicClasses
import java.io.Closeable
import java.lang.reflect.InvocationTargetException
import java.net.InetSocketAddress
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.filter
import kotlin.collections.forEach
import kotlin.collections.set
import kotlin.reflect.KClass


class WebSocketRpcServer constructor(
    val host: String = "localhost",
    val port: Int,

    val pingTimeout: Int = 10, // in seconds
    val isReuseAddress: Boolean = true,
    val isTcpNoDelayActive: Boolean = false
) : Closeable {
    companion object {
        const val SERVICE_ENDPOINT_NAME = "Service"
        val SERVICE_ENDPOINT_UUID = "ffffffff-ffff-ffff-ffff-ffffffffffff".toUUID()

        val log = logger(FINER)

        internal val serializers = dictionaryOf<KClass<*>, (Any) -> WebSocketRpcEndpoint>()

        fun <T: Any> registerTypeAdapter(kClass: KClass<T>, apiGen: (T) -> WebSocketRpcEndpoint) {
            check(kClass !in serializers) {
                "Can't set global serializer for class $kClass because it was already specified"
            }
            @Suppress("UNCHECKED_CAST")
            serializers[kClass] = apiGen as (Any) -> WebSocketRpcEndpoint
        }
    }

    private val myEndpoints = ConcurrentHashMap<UUID, EndpointHolder>()

    fun register(endpoint: WebSocketRpcEndpoint, uuid: UUID? = null): EndpointHolder {
        val actual = uuid ?: uuid()

        require(!myEndpoints.contains(actual)) { "Endpoint with $uuid already registered!" }

        return EndpointHolder(this, endpoint, actual).also {
            myEndpoints[actual] = it
            it.onRegister()
            log.finest { "Endpoint $it registered" }
        }
    }

    fun unregister(uuid: UUID) {
        require(uuid != SERVICE_ENDPOINT_UUID) { "Can't unregister service endpoint!" }

        myEndpoints.remove(uuid)
            .sure { "Endpoint with $uuid not registered!" }
            .also {
                it.onUnregister()
                log.finest { "Endpoint $it unregistered" }
            }
    }

    val address = InetSocketAddress(host, port)

    private val threads = newFixedThreadPoolDispatcher(availableProcessors)

    private val server = object : WebSocketServer(address) {
        private val mapper = GsonBuilder()
            .registerBasicClasses()
            .serializeNulls()
            .create()

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

    override fun close() = stop()

    internal val resources = ResourceManager()

    inner class ServiceEndpoint(override val name: String = SERVICE_ENDPOINT_NAME) : WebSocketRpcEndpoint {
        @WebSocketRpcMethod
        fun endpoints() = myEndpoints.associate { it.key to it.value.identifier }

        @WebSocketRpcMethod
        fun clear() = myEndpoints.values
            .filter { it.endpoint != this }
            .forEach { this@WebSocketRpcServer.unregister(it.uuid) }

        @WebSocketRpcMethod
        fun unregister(uuid: UUID) = this@WebSocketRpcServer.unregister(uuid)

        @WebSocketRpcMethod
        fun address() = server.address

        @WebSocketRpcMethod
        fun port() = server.port
    }

    init {
        register(ServiceEndpoint(), SERVICE_ENDPOINT_UUID)
    }
}