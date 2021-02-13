package ru.inforion.lab403.common.extensions.serialization

import java.io.OutputStream

internal class BytesOutputStream(size: Int) : OutputStream() {
    init {
        require(size > 0) { "Negative initial size: $size" }
    }

    val array = ByteArray(size)

    private var position = 0

    override fun write(b: Int) {
        array[position++] = b.toByte()
    }
}