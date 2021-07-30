package ru.inforion.lab403.common.wsrpc

import ru.inforion.lab403.common.concurrent.events.SimpleEvent
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.wsrpc.annotations.WebSocketRpcMethod
import ru.inforion.lab403.common.wsrpc.interfaces.WebSocketRpcEndpoint
import ru.inforion.lab403.common.wsrpc.sequence.SerializableSequence.Companion.asSerializableSequence
import kotlin.concurrent.thread

object SimpleServer {
    val log = logger()

    data class Developer(val name: String, val surname: String, val codes: String)

    val test = object : WebSocketRpcEndpoint {
        override val name = "test"

        val notification = SimpleEvent()

        var counter = 0

//        val t = thread(isDaemon = true) {
//            while (true) {
//                log.info { "Notify: counter = ${counter++}" }
//                notification.signal()
//                Thread.sleep(1000)
//            }
//        }

        val developers = listOf(
            Developer("Morgan", "Freeman", "123"),
            Developer("Leonardo", "DiCaprio", "234"),
            Developer("Michael", "Caine", "563"),
            Developer("Matt", "Damon", "1010"),
            Developer("Christian", "Bale", "303030001")
        )

        @WebSocketRpcMethod
        fun length(array: ByteArray) = array.size

        @WebSocketRpcMethod
        fun event() = notification

        @WebSocketRpcMethod
        fun developers() = developers.asSerializableSequence()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val rpc = WebSocketRpcServer(port = 6901).apply {
            register(test)
        }
        rpc.start()
    }
}