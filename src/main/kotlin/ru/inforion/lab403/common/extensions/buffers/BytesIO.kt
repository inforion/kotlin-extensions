package ru.inforion.lab403.common.extensions.buffers

interface BytesIO {
    val writeAvailable: Int

    val readAvailable: Int

    fun write(bytes: ByteArray, offset: Int = 0, count: Int = bytes.size)

    fun read(count: Int): ByteArray
}