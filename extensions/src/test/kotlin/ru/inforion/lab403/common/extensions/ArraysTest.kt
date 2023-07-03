package ru.inforion.lab403.common.extensions

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.inforion.lab403.common.utils.Benchmark
import java.nio.ByteOrder.BIG_ENDIAN
import java.nio.ByteOrder.LITTLE_ENDIAN
import kotlin.system.measureTimeMillis

internal class ArraysTest {
    data class TestClass(val int: Int, val long: Long, val string: String)

    val correctValueSumbyLong = 15L
    private val x = arrayOf(0, 1, 2, 3, 4, 5)
    private val b = byteArrayOf(0, 1, 2, 3, 4, 5)

    @Test fun getTest_p0_p1() = assertArrayEquals(arrayOf(0, 1), x[0..1])
    @Test fun getTest_p2_p3() = assertArrayEquals(arrayOf(2, 3), x[2..3])
    @Test fun getTest_p0_m0() = assertArrayEquals(arrayOf(0, 1, 2, 3, 4, 5), x[0..0])
    @Test fun getTest_p0_m1() = assertArrayEquals(arrayOf(0, 1, 2, 3, 4, 5, 0), x[0..-1])
    @Test fun getTest_p3_m0() = assertArrayEquals(arrayOf(3, 4, 5), x[3..0])
    @Test fun getTest_p3_m1() = assertArrayEquals(arrayOf(3, 4, 5, 0), x[3..-1])
    @Test fun getTest_p4_m3() = assertArrayEquals(arrayOf(4, 5, 0, 1, 2), x[4..-3])
    @Test fun getTest_p4_m4() = assertArrayEquals(arrayOf(4, 5, 0, 1, 2, 3), x[4..-4])

    @Test fun getBytesTest_p0_p1() = assertArrayEquals(byteArrayOf(0, 1), b[0..1])
    @Test fun getBytesTest_p2_p3() = assertArrayEquals(byteArrayOf(2, 3), b[2..3])
    @Test fun getBytesTest_p0_m0() = assertArrayEquals(byteArrayOf(0, 1, 2, 3, 4, 5), b[0..0])
    @Test fun getBytesTest_p0_m1() = assertArrayEquals(byteArrayOf(0, 1, 2, 3, 4, 5, 0), b[0..-1])
    @Test fun getBytesTest_p3_m0() = assertArrayEquals(byteArrayOf(3, 4, 5), b[3..0])
    @Test fun getBytesTest_p3_m1() = assertArrayEquals(byteArrayOf(3, 4, 5, 0), b[3..-1])
    @Test fun getBytesTest_p4_m3() = assertArrayEquals(byteArrayOf(4, 5, 0, 1, 2), b[4..-3])
    @Test fun getBytesTest_p4_m4() = assertArrayEquals(byteArrayOf(4, 5, 0, 1, 2, 3), b[4..-4])

    @Test fun startswithTest0() = assertTrue(byteArrayOf(1,2,3,4).startswith(byteArrayOf(1,2)))
    @Test fun startswithTest1() = assertTrue(byteArrayOf(3,4).startswith(byteArrayOf(3,4)))
    @Test fun startswithTest2() = assertFalse(byteArrayOf(10).startswith(byteArrayOf(20,1)))
    @Test fun startswithTest3() = assertFalse(byteArrayOf(5,6,7,8).startswith(byteArrayOf(1,2)))
    @Test fun startswithTest5() = assertFalse(byteArrayOf().startswith(byteArrayOf(1,2)))
    @Test fun startswithTest6() = assertTrue(byteArrayOf().startswith(byteArrayOf()))

    @Test fun startswithTest7() = assertTrue(byteArrayOf(1,5,6,7).startswith(byteArrayOf(1,2), 1))
    @Test fun startswithTest8() = assertTrue(byteArrayOf(3,6).startswith(byteArrayOf(3,4), 1))
    @Test fun startswithTest9() = assertFalse(byteArrayOf(10).startswith(byteArrayOf(20,1), 1))
    @Test fun startswithTest10() = assertFalse(byteArrayOf(5,6,1,2).startswith(byteArrayOf(1,2), 2))
    @Test fun startswithTest12() = assertFalse(byteArrayOf().startswith(byteArrayOf(1,2), 1))

    @Test fun startswithTest13() = assertTrue(byteArrayOf(9, 8, 7, 6).startswith(byteArrayOf(9, 8, 0, 0, 0, 0, 0), 2))

    lateinit var array: ByteArray

    @Before fun initTest() {
        array = "DEADBEEF CAFEAFFE BAADF00DAABBCCDD".unhexlify()
    }

    fun assertPut(expected: String, actual: ByteArray) = assertEquals(expected.replace(" ", "").uppercase(), actual.hexlify())
    fun assertGet(expected: String, actual: Long) = assertEquals(expected.replace(" ", "").uppercase(), actual.hex16)

    @Test fun putInt64BigEndian() {
        array.putInt(2, 0x00112233_44556677L, 8, BIG_ENDIAN)
        assertPut("DEAD 0011223344556677 F00DAABBCCDD", array)
    }

    @Test fun putInt64LittleEndian() {
        array.putInt(2, 0x00112233_44556677L, 8, LITTLE_ENDIAN)
        assertPut("DEAD 7766554433221100 F00DAABBCCDD", array)
    }

    @Test fun putInt32BigEndian() {
        array.putInt(6, 0x00112233, 4, BIG_ENDIAN)
        assertPut("DEADBEEFCAFE 00112233 F00DAABBCCDD", array)
    }

    @Test fun putInt32LittleEndian() {
        array.putInt(6, 0x00112233, 4, LITTLE_ENDIAN)
        assertPut("DEADBEEFCAFE 33221100 F00DAABBCCDD", array)
    }

    @Test fun putInt24BigEndian() {
        array.putInt(8, 0x11EEFF, 3, BIG_ENDIAN)
        assertPut("DEADBEEFCAFEAFFE 11EEFF 0DAABBCCDD", array)
    }

    @Test fun putInt24LittleEndian() {
        array.putInt(8, 0x11EEFF, 3, LITTLE_ENDIAN)
        assertPut("DEADBEEFCAFEAFFE FFEE11 0DAABBCCDD", array)
    }

    @Test fun putInt16BigEndian() {
        array.putInt(9, 0xEEFF, 2, BIG_ENDIAN)
        assertPut("DEADBEEFCAFEAFFEBA EEFF 0DAABBCCDD", array)
    }

    @Test fun putInt16LittleEndian() {
        array.putInt(9, 0xEEFF, 2, LITTLE_ENDIAN)
        assertPut("DEADBEEFCAFEAFFEBA FFEE 0DAABBCCDD", array)
    }

    @Test fun putInt8() {
        array.putInt(11, 0xFF, 1, LITTLE_ENDIAN)
        assertPut("DEADBEEFCAFEAFFEBAADF0 FF AABBCCDD", array)
    }

    @Test fun getInt64BigEndian() {
        val data = array.getInt(3, 8, BIG_ENDIAN)
        assertGet("EFCAFEAFFEBAADF0", data)
    }

    @Test fun getInt64LittleEndian() {
        val data = array.getInt(3, 8, LITTLE_ENDIAN)
        assertGet("F0ADBAFEAFFECAEF", data)
    }

    @Test fun getInt32BigEndian() {
        val data = array.getInt(4, 4, BIG_ENDIAN)
        assertGet("0000 0000 CAFEAFFE", data)
    }

    @Test fun getInt32LittleEndian() {
        val data = array.getInt(4, 4, LITTLE_ENDIAN)
        assertGet("0000 0000 FEAFFECA", data)
    }

    @Test fun getInt24BigEndian() {
        val data = array.getInt(4, 3, BIG_ENDIAN)
        assertGet("0000 0000 00 CAFEAF", data)
    }

    @Test fun getInt24LittleEndian() {
        val data = array.getInt(4, 3, LITTLE_ENDIAN)
        assertGet("0000 0000 00 AFFECA", data)
    }

    @Test fun getInt16BigEndian() {
        val data = array.getInt(6, 2, BIG_ENDIAN)
        assertGet("0000 0000 0000 AFFE", data)
    }

    @Test fun getInt16LittleEndian() {
        val data = array.getInt(6, 2, LITTLE_ENDIAN)
        assertGet("0000 0000 0000 FEAF", data)
    }

    @Test fun getInt8() {
        val data = array.getInt(7, 1, BIG_ENDIAN)
        assertGet("0000 0000 00 00 00 FE", data)
    }

    @Test fun getDouble() {
        println("ByteArray.getDouble() not yet tested!")
    }

    @Test fun putDouble() {
        println("ByteArray.putDouble() not yet tested!")
    }

    @Test fun subByLongByteArray() {
        val sum = byteArrayOf(0, 1, 2, 3, 4, 5).sumOf { it }.long_s
        assertEquals("Error summing: expected: $correctValueSumbyLong, real: $sum", correctValueSumbyLong, sum)
    }

    @Test fun subByLongIntArray() {
        val sum = intArrayOf(0, 1, 2, 3, 4, 5).sumOf { it.long_s }
        assertEquals("Error summing: expected: $correctValueSumbyLong, real: $sum", correctValueSumbyLong, sum)
    }

    @Test fun subByLongLongArray() {
        val sum = longArrayOf(0, 1, 2, 3, 4, 5).sumOf { it }
        assertEquals("Error summing: expected: $correctValueSumbyLong, real: $sum", correctValueSumbyLong, sum)
    }

    @Test fun subByLongDataClassArray() {
        val sum = arrayOf(
                TestClass(0, 0, "0"),
                TestClass(1, 1, "1"),
                TestClass(2, 2, "2"),
                TestClass(3, 3, "3"),
                TestClass(4, 4, "4"),
                TestClass(5, 5, "5")
        ).sumOf { it.long * 2 }
        assertEquals("Error summing: expected: ${correctValueSumbyLong * 2}, real: $sum", correctValueSumbyLong * 2, sum)
    }

    @Test fun subByLongDataClassList() {
        val sum = listOf(
                TestClass(0, 0, "0"),
                TestClass(1, 1, "1"),
                TestClass(2, 2, "2"),
                TestClass(3, 3, "3"),
                TestClass(4, 4, "4"),
                TestClass(5, 5, "5")
        ).sumOf { it.long * 2 }
        assertEquals("Error summing: expected: ${correctValueSumbyLong * 2}, real: $sum", correctValueSumbyLong * 2, sum)
    }

    @Test fun justPerformanceLittle() {
        Benchmark().bench {
            array.putInt(2, 0x00112233_44556677L, 8, LITTLE_ENDIAN)
            array.putInt(6, 0x00112233, 4, LITTLE_ENDIAN)
            array.putInt(8, 0x11EEFF, 3, LITTLE_ENDIAN)
        }.also {
            println("mean: ${it.inWholeMilliseconds}")
        }
    }

    @Test fun justPerformanceBig() {
        Benchmark().bench {
            array.putInt(2, 0x00112233_44556677L, 8, BIG_ENDIAN)
            array.putInt(6, 0x00112233, 4, BIG_ENDIAN)
            array.putInt(8, 0x11EEFF, 3, BIG_ENDIAN)
        }.also {
            println("mean: ${it.inWholeMilliseconds}")
        }
    }
}