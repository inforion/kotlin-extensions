package ru.inforion.lab403.common.iobuffer

import org.junit.jupiter.api.Test
import ru.inforion.lab403.common.extensions.byte
import ru.inforion.lab403.common.extensions.int_z
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue


internal class CircularBytesIOTest {
    @Test
    fun writeAvailableNotFullTest() {
        val bytes = ByteArray(12) { it.byte }
        val io = CircularBytesIO(16)
        assertEquals(0, io.readAvailable)
        assertEquals(16, io.writeAvailable)
        io.write(bytes)
        assertEquals(4, io.writeAvailable)
        assertEquals(12, io.readAvailable)
    }

    @Test
    fun writeAvailableFullTest() {
        val bytes = ByteArray(16) { it.byte }
        val io = CircularBytesIO(16)
        assertEquals(0, io.readAvailable)
        assertEquals(16, io.writeAvailable)
        io.write(bytes)
        assertEquals(0, io.writeAvailable)
        assertEquals(16, io.readAvailable)
    }

    @Test
    fun writeOverflowTest() {
        val bytes = ByteArray(20) { it.byte }
        val io = CircularBytesIO(16)
        assertEquals(0, io.readAvailable)
        assertEquals(16, io.writeAvailable)
        assertFails { io.write(bytes) }
    }

    @Test
    fun writeReadNotFullTest() {
        val bytes = ByteArray(12) { it.byte }
        val io = CircularBytesIO(16)

        assertEquals(0, io.readAvailable)
        assertEquals(16, io.writeAvailable)

        io.write(bytes)
        val actual = io.read(7)

        assertEquals(5, io.readAvailable)
        assertEquals(11, io.writeAvailable)
        assertEquals(listOf(0, 1, 2, 3, 4, 5, 6), actual.map { it.int_z })
    }

    @Test
    fun writeReadCircularTest() {
        val chunk0 = ByteArray(12) { it.byte }
        val io = CircularBytesIO(16)

        assertEquals(0, io.readAvailable)
        assertEquals(16, io.writeAvailable)

        io.write(chunk0)
        val actual = io.read(7)

        val chunk1 = ByteArray(11) { it.byte }
        io.write(chunk1)

        assertEquals(16, io.readAvailable)
        assertEquals(0, io.writeAvailable)

        io.read(3)

        assertEquals(13, io.readAvailable)
        assertEquals(3, io.writeAvailable)

        io.read(13)

        assertEquals(0, io.readAvailable)
        assertEquals(16, io.writeAvailable)

        io.write(chunk1, 0, 9)

        assertEquals(9, io.readAvailable)
        assertEquals(7, io.writeAvailable)

        io.write(chunk1, 0, 3)

        assertEquals(12, io.readAvailable)
        assertEquals(4, io.writeAvailable)

        io.read(11)

        assertEquals(1, io.readAvailable)
        assertEquals(15, io.writeAvailable)

        io.write(chunk1, 0, 11)

        assertEquals(12, io.readAvailable)
        assertEquals(4, io.writeAvailable)

        io.read(4)
        io.read(4)
        io.read(4)

        assertEquals(0, io.readAvailable)
        assertEquals(16, io.writeAvailable)

        val chunk3 = ByteArray(20) { it.byte }
        io.write(chunk3, 0, 16)

        assertEquals(16, io.readAvailable)
        assertEquals(0, io.writeAvailable)
    }

    @Test
    fun writeReadCircularDataTest() {
        val chunk = ByteArray(20) { it.byte }

        val io = CircularBytesIO(16)
        io.write(chunk, 0, 10)
        io.read(6)
        io.write(chunk, 0, 10)
        val actual = io.read(14)

        assertEquals(listOf(6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9), actual.map { it.int_z })

        assertEquals(0, io.readAvailable)
        assertEquals(16, io.writeAvailable)
    }

    @Test
    fun clearCircularBufferTest() {
        val chunk = ByteArray(20) { it.byte }

        val io = CircularBytesIO(20)
        io.write(chunk, 0, 10)
        io.write(chunk, 0, 10)

        assertEquals(20, io.readAvailable)
        assertEquals(0, io.writeAvailable)

        io.clear()

        assertEquals(0, io.readAvailable)
        assertEquals(20, io.writeAvailable)
    }

    @Test
    fun iterateCircularBufferTest() {
        val chunk = ByteArray(20) { it.byte }

        val io = CircularBytesIO(20)
        io.write(chunk, 0, 10)
        io.read(8)
        io.write(chunk, 0, 10)

        val index = io.indexOf(5)

        assertEquals(7, index)
        assertEquals(4, io.readAvailable)

        val actual = io.map { it * it }
        val expected = listOf(36, 49, 64, 81)

        assertEquals(expected, actual)

        assertEquals(0, io.readAvailable)
        assertEquals(20, io.writeAvailable)
    }

    @Test
    fun isEmptyCircularBufferTest() {
        val chunk = ByteArray(20) { it.byte }

        val io = CircularBytesIO(20)
        io.write(chunk, 0, 10)
        io.write(chunk, 0, 10)

        io.clear()

        assertTrue { io.isEmpty() }
    }
}