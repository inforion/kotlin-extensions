package ru.inforion.lab403.common.extensions

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.*
import java.nio.ByteBuffer
import kotlin.test.assertEquals

internal class ByteBufferTest {
    @Test
    fun bufferHeapSerializationTest() {
        val buffer = ByteBuffer.allocate(0x100_0000)

        repeat(100) { buffer.put(it.byte) }

        val bos = ByteArrayOutputStream()
        val output = DataOutputStream(bos)
        output.writeInt(100)
        output.writeByteBuffer(buffer)
        output.writeInt(200)

        val array = bos.toByteArray()

        val input = DataInputStream(array.inputStream())
        input.readInt()
        val result = input.readByteBuffer()
        input.readInt()

        assertEquals(buffer.array().hexlify(), result.array().hexlify())
    }

    @Test
    fun bufferDirectedSerializationTest() {
        val buffer = ByteBuffer.allocateDirect(0x100_0000)

        repeat(100) { buffer.put(it.byte) }

        buffer.position(0)
        val array0 = ByteArray(buffer.limit()) { buffer.get() }

        val stream = ByteArrayOutputStream()
        DataOutputStream(stream).writeByteBuffer(buffer)
        val result = DataInputStream(stream.toByteArray().inputStream()).readByteBuffer()

        result.position(0)
        val array1 = ByteArray(result.limit()) { result.get() }

        assertEquals(array0.hexlify(), array1.hexlify())
    }

    @Test
    fun bufferDirectedThrowTest() {
        val buffer = ByteBuffer.allocateDirect(0x1000_0000)
        val stream = ByteArrayOutputStream()
        DataOutputStream(stream).writeByteBuffer(buffer)
        assertThrows<OutOfMemoryError> {
            DataInputStream(stream.toByteArray().inputStream()).readByteBuffer()
        }
    }
}