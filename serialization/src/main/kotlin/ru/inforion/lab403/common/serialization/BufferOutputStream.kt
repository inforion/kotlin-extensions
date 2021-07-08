package ru.inforion.lab403.common.serialization

import ru.inforion.lab403.common.extensions.byte
import ru.inforion.lab403.common.extensions.byteBuffer
import java.io.OutputStream
import java.nio.ByteBuffer

class BufferOutputStream(val buffer: ByteBuffer) : OutputStream() {
    constructor(size: Int, directed: Boolean) : this(byteBuffer(size, directed))

    override fun write(b: Int): Unit = run { buffer.put(b.byte) }
}