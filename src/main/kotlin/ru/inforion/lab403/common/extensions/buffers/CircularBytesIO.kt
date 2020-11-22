package ru.inforion.lab403.common.extensions.buffers

import ru.inforion.lab403.common.logging.INFO
import ru.inforion.lab403.common.logging.logger

/**
 * Bytes buffer input-output implementation
 *
 * @since 0.3.4
 */
class CircularBytesIO(val capacity: Int) : BytesIO {
    companion object {
        val log = logger(INFO)
    }

    private val data = ByteArray(capacity)

    private var writePos = 0
    private var readPos = 0

    /*
     +++++++++++++w..............r+++++++++++++++++++..
     --------------------------------------------------|

     .........r++++++++++++++++w.......................
     --------------------------------------------------|

     w         w
     r.........r.......................................
     --------------------------------------------------|
     0                                                 cap
    */

    private var writeOverflow = false

    override val writeAvailable get() = when {
        writePos < readPos || writeOverflow -> readPos - writePos
        else -> capacity - writePos + readPos
    }

    override val readAvailable get() = when {
        readPos <= writePos && !writeOverflow -> writePos - readPos
        else -> capacity - readPos + writePos
    }

    private fun writeIntern(bytes: ByteArray, offset: Int, count: Int): Int {
        log.finest { "entr -> wpos=$writePos wa=$writeAvailable ra=$readAvailable ovf=$writeOverflow offset=$offset count=$count" }

        require(writePos + count <= capacity) { "Can't write chunk [pos=$writePos count=$count]" }

        bytes.copyInto(data, writePos, offset, offset + count)
        writePos = (writePos + count) % capacity
        writeOverflow = writePos == readPos

        log.finest { "exit -> wpos=$writePos wa=$writeAvailable ra=$readAvailable ovf=$writeOverflow" }

        return count
    }

    private fun readIntern(count: Int): ByteArray {
        require(readPos + count <= capacity) { "Can't read chunk [pos=$readPos count=$count]" }

        log.finest { "entr -> rpos=$readPos wa=$writeAvailable ra=$readAvailable ovf=$writeOverflow count=$count" }

        val result = data.copyOfRange(readPos, readPos + count)
        readPos = (readPos + count) % capacity
        if (readPos == writePos)
            writeOverflow = false

        log.finest { "exit -> rpos=$readPos wa=$writeAvailable ra=$readAvailable ovf=$writeOverflow" }

        return result
    }

    override fun write(bytes: ByteArray, offset: Int, count: Int) {
        log.finer { "entr -> wpos=$writePos wa=$writeAvailable ra=$readAvailable ovf=$writeOverflow count=$count " }

        require(count <= bytes.size) { "Input array size=${bytes.size} < count=$count" }
        require(count <= writeAvailable) { "Not enough space for write [count=$count available=${writeAvailable}]" }

        if (writePos + count > capacity) {
            val written = writeIntern(bytes, offset, capacity - writePos)
            writeIntern(bytes, offset + written, count - written)
        } else {
            writeIntern(bytes, offset, count)
        }

        check(writeAvailable >= 0) { "Internal write operations failed -> writeAvailable=$writeAvailable" }

        log.finer { "exit -> wpos=$writePos wa=$writeAvailable ra=$readAvailable ovf=$writeOverflow" }
    }

    override fun read(count: Int): ByteArray {
        log.finer { "entr -> rpos=$readPos wa=$writeAvailable ra=$readAvailable ovf=$writeOverflow count=$count" }

        require(count <= readAvailable) { "Not enough space for read [count=$count available=${readAvailable}]" }

        return if (readPos + count > capacity) {
            val chunk0 = readIntern(capacity - readPos)
            val chunk1 = readIntern(count - chunk0.size)
            chunk0 + chunk1
        } else {
            readIntern(count)
        }.also {
            log.finer { "exit -> rpos=$readPos wa=$writeAvailable ra=$readAvailable ovf=$writeOverflow" }

            check(readAvailable >= 0) { "Internal read operations failed -> readAvailable=$readAvailable" }
        }
    }
}