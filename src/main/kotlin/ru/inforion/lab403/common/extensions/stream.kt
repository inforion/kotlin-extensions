package ru.inforion.lab403.common.extensions

import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer

/**
 * Created by Vladimir Davydov on 22.06.16.
 */
val BUFFER_SIZE = 8 * 1024 * 1024


fun InputStream.readInto(dst: ByteBuffer, offset: Int = 0): Int {
    var total = 0
    val buf = ByteArray(BUFFER_SIZE)
    dst.position(offset)
    do {
        val count = this.read(buf, 0, BUFFER_SIZE)
        if (count > 0) {
            dst.put(buf, 0, count)
            total += count
        } else break
    } while (count > 0)
    return total
}

fun OutputStream.writeFrom(src: ByteBuffer, offset: Int = 0) {
    src.position(offset)
    val buffer = ByteArray(BUFFER_SIZE)
    do {
        val count = Math.min(BUFFER_SIZE, src.remaining())
        src.get(buffer, 0, count)
        this.write(buffer, 0, count)
    } while (src.remaining() != 0)
}