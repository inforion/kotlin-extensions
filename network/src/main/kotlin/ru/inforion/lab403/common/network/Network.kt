package ru.inforion.lab403.common.network

import ru.inforion.lab403.common.logging.CONFIG
import ru.inforion.lab403.common.logging.logStackTrace
import ru.inforion.lab403.common.logging.logger
import java.io.Closeable
import java.net.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Semaphore
import kotlin.concurrent.thread


class Network(
    val desiredPort: Int,
    val name: String,
    val bufSize: Int = 1024,
    val maxClients: Int = 10,
    start: Boolean = true
) : Closeable {
    companion object {
        @Transient
        private val log = logger(CONFIG)
    }

    private val server = ServerSocket()

    private var running = false

    val address: String get() = InetAddress.getLocalHost().hostAddress
    val port get() = server.localPort

    private var onStart: OnStartCallback? = null
    private var onConnect: OnConnectCallback? = null
    private var onDisconnect: OnDisconnectCallback? = null
    private var onReceive: OnReceiveCallback? = null

    fun onStart(callback: OnStartCallback) = apply { onStart = callback }

    fun onConnect(callback: OnConnectCallback) = apply { onConnect = callback }

    fun onDisconnect(callback: OnDisconnectCallback) = apply { onDisconnect = callback }

    fun onReceive(callback: OnReceiveCallback) = apply { onReceive = callback }

    private fun Socket.process() {
        val buf = ByteArray(bufSize)

        do {
            var bytes = inputStream.read(buf)
            while (inputStream.available() > 0 && bytes < bufSize)
                bytes += inputStream.read(buf, bytes, bufSize - bytes)

            val connected = if (bytes > 0) {
                val data = ByteArray(bytes)
                System.arraycopy(buf, 0, data, 0, bytes)
                onReceive?.invoke(this, data) ?: true
            } else false
        } while (connected)
    }

    private val semaphore = Semaphore(maxClients)

    private val connections = ConcurrentHashMap<Socket, Thread>()

    private fun serve() = server
        .runCatching {
            semaphore.acquire()
            log.info { "$name waited for clients on [$address:$port]" }
            accept()
        }.onSuccess { client ->
            log.info { "$name: client $client connected" }
            thread {
                // See https://en.wikipedia.org/wiki/Nagle%27s_algorithm
                client.tcpNoDelay = true

                client
                    .runCatching {
                        val process = onConnect?.invoke(this) ?: true
                        if (process) client.process()
                        onDisconnect?.invoke(client)
                    }.onFailure { error ->
                        error.logStackTrace(log)
                    }

                semaphore.release()
                client.close()
                connections.remove(client)
            }.also {
                connections[client] = it
            }
        }.onFailure { error ->
            log.info { "$name: connection closed due to $error" }
        }.isSuccess

    private val thread = thread(start, name = name) {
        val address = InetSocketAddress(desiredPort)
        server.bind(address)
        onStart?.invoke(server.inetAddress)
        running = true
        while (running) serve()
        log.info { "$name stopped" }
    }

    fun start() = thread.start()

    override fun close() {
        running = false

        server.close()
        thread.join()

        connections.keys
            .mapNotNull { client ->
                client.close()
                connections[client]
            }.forEach { it.join() }
    }

    override fun toString() = "$name(port=$port,alive=${thread.isAlive})"
}