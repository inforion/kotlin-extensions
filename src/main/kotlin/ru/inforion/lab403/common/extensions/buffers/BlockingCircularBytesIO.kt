package ru.inforion.lab403.common.extensions.buffers

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Blocking bytes buffer input-output implementation
 *
 * @since 0.3.4
 */
class BlockingCircularBytesIO(val capacity: Int) : BlockingBytesIO {
    private val io = CircularBytesIO(capacity)

    private val lock = ReentrantLock()

    /** Condition for waiting takes  */
    private val bytesWritten = lock.newCondition()

    /** Condition for waiting puts  */
    private val bytesRead = lock.newCondition()

    private fun signalWrite(bytes: ByteArray, offset: Int, count: Int) =
        io.write(bytes, offset, count).also { bytesWritten.signal() }

    private fun signalRead(count: Int) = io.read(count).also { bytesRead.signal() }

    private inline fun <T> locked(action: () -> T) = lock.withLock { action() }

    override val writeAvailable get() = locked { io.writeAvailable }

    override val readAvailable get() = locked { io.readAvailable }

    // non-blocking methods
    override fun offer(bytes: ByteArray, offset: Int, count: Int) = locked {
        count.coerceAtMost(io.writeAvailable).also { signalWrite(bytes, offset, it) }
    }

    override fun poll(count: Int) = locked {
        signalRead(count.coerceAtMost(io.readAvailable))
    }

    // non-blocking timeout methods
    override fun offer(bytes: ByteArray, offset: Int, count: Int, timeout: Long, unit: TimeUnit): Int {
        var nanos = unit.toNanos(timeout)
        return locked {
            var written = 0
            while (written != count) {
                if (io.writeAvailable == 0) {
                    nanos = bytesRead.awaitNanos(nanos)
                    if (nanos <= 0L) break
                }
                val toWrite = (count - written).coerceAtMost(io.writeAvailable)
                signalWrite(bytes, offset + written, toWrite)
                written += toWrite
            }
            written
        }
    }

    override fun poll(count: Int, timeout: Long, unit: TimeUnit): ByteArray {
        var nanos = unit.toNanos(timeout)
        return locked {
            var result = byteArrayOf()
            while (result.size != count) {
                if (io.readAvailable == 0) {
                    nanos = bytesWritten.awaitNanos(nanos)
                    if (nanos <= 0L) break
                }
                val toRead = (count - result.size).coerceAtMost(io.readAvailable)
                result += signalRead(toRead)
            }
            result
        }
    }

    // blocking methods
    override fun put(bytes: ByteArray, offset: Int, count: Int) = locked {
        while (io.writeAvailable < count) bytesRead.await()
        signalWrite(bytes, offset, count)
    }

    override fun take(count: Int) = locked {
        while (io.readAvailable < count) bytesWritten.await()
        signalRead(count)
    }

    // exception throw methods
    override fun write(bytes: ByteArray, offset: Int, count: Int) = locked { signalWrite(bytes, offset, count) }

    override fun read(count: Int) = locked { signalRead(count) }
}