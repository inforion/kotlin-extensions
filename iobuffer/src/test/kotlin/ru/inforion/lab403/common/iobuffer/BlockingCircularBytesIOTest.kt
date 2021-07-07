package ru.inforion.lab403.common.iobuffer

import org.junit.Test
import ru.inforion.lab403.common.concurrent.async
import ru.inforion.lab403.common.concurrent.wait
import ru.inforion.lab403.common.extensions.*
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.expect


internal class BlockingCircularBytesIOTest {
    @Test
    fun takePutTest() {
        val chunk = ByteArray(20) { it.byte }
        val io = BlockingCircularBytesIO(16)

        val job = async { io.take(10) }

        io.put(chunk, 0, 12)

        expect(listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)) { job.wait().map { it.int_s } }
    }

    @Test
    fun pollFullPutTest() {
        val chunk = ByteArray(20) { it.byte }
        val io = BlockingCircularBytesIO(16)

        val job = async { io.poll(10, 1000) }

        io.put(chunk, 0, 10)

        expect(listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)) { job.wait().map { it.int_s } }
    }

    @Test
    fun pollPartPutTest() {
        val chunk = ByteArray(20) { it.byte }
        val io = BlockingCircularBytesIO(16)

        val job = async { io.poll(10, 1000) }

        io.put(chunk, 0, 5)

        expect(listOf(0, 1, 2, 3, 4)) { job.wait().map { it.int_s } }
    }

    @Test
    fun offerLargeTakeTest() {
        val chunk = ByteArray(20) { it.byte }
        val io = BlockingCircularBytesIO(16)

        val job = async { io.offer(chunk, timeout = 1000) }

        val actual = List(5) { io.take(4) }.map { byte -> byte.map { it.int_z } }

        expect(20) { job.wait() }

        assertEquals(listOf(0, 1, 2, 3), actual[0])
        assertEquals(listOf(4, 5, 6, 7), actual[1])
        assertEquals(listOf(8, 9, 10, 11), actual[2])
        assertEquals(listOf(12, 13, 14, 15), actual[3])
        assertEquals(listOf(16, 17, 18, 19), actual[4])
    }

    @Test
    fun offerLargePollTest() {
        val chunk = ByteArray(20) { it.byte }
        val io = BlockingCircularBytesIO(16)

        val job = async { io.offer(chunk, timeout = 1000) }

        val actual = List(5) { io.poll(8, 1000) }.map { byte -> byte.map { it.int_z } }

        expect(20) { job.wait() }

        assertEquals(listOf(0, 1, 2, 3, 4, 5, 6, 7), actual[0])
        assertEquals(listOf(8, 9, 10, 11, 12, 13, 14, 15), actual[1])
        assertEquals(listOf(16, 17, 18, 19), actual[2])
    }

    @Test
    fun writeFailTest() {
        val chunk = ByteArray(20) { it.byte }
        val io = BlockingCircularBytesIO(16)
        assertFails { io.write(chunk) }
    }

    @Test
    fun readFailTest() {
        val io = BlockingCircularBytesIO(16)
        assertFails { io.read(10) }
    }
}