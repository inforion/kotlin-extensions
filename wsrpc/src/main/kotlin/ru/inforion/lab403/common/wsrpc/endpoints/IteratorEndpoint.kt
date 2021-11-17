package ru.inforion.lab403.common.wsrpc.endpoints

import ru.inforion.lab403.common.concurrent.locks.PhonyLock
import ru.inforion.lab403.common.extensions.cast
import ru.inforion.lab403.common.wsrpc.annotations.WebSocketRpcMethod
import ru.inforion.lab403.common.wsrpc.interfaces.WebSocketRpcEndpoint
import java.util.concurrent.locks.Lock
import kotlin.concurrent.withLock

class IteratorEndpoint<T> constructor(
    iterator: Iterator<T>,
    private val lock: Lock = PhonyLock,
    override val name: String = "Iterator",
): WebSocketRpcEndpoint {
    companion object {
        inline fun <T> Sequence<T>.toIterableEndpoint(size: Int = 64, lock: Lock = PhonyLock, name: String = "Iterator") =
            IteratorEndpoint(windowed(size, size, partialWindows = true).iterator(), lock, name)
    }

    private var iter: Iterator<*> = iterator

    @WebSocketRpcMethod
    fun hasNext(): Boolean = lock.withLock { iter.hasNext() }

    @WebSocketRpcMethod
    fun next(): T? = if (iter.hasNext()) iter.next().cast() else null

}