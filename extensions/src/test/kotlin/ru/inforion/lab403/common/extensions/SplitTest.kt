package ru.inforion.lab403.common.extensions

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SplitTest {

    companion object {

        private val listOfValues = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    }

    @Test
    fun split1Test() {
        val splitList = listOfValues.split(1)

        assertEquals(listOf(listOfValues), splitList)
    }

    @Test
    fun split2Test() {
        val splitList = listOfValues.split(2)
        val expected = listOf(listOf(1, 2, 3, 4, 5), listOf(6, 7, 8, 9, 10))
        assertEquals(expected, splitList)
    }

    @Test
    fun split3Test() {
        val splitList = listOfValues.split(3)
        val expected = listOf(listOf(1, 2, 3, 4), listOf(5, 6, 7), listOf(8, 9, 10))
        assertEquals(expected, splitList)
    }

    @Test
    fun split4Test() {
        val splitList = listOfValues.split(4)
        val expected = listOf(listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8), listOf(9, 10))
        assertEquals(expected, splitList)
    }

    @Test
    fun split5Test() {
        val splitList = listOfValues.split(5)
        val expected = listOf(listOf(1, 2), listOf(3, 4), listOf(5, 6), listOf(7, 8), listOf(9, 10))
        assertEquals(expected, splitList)
    }

    @Test
    fun split6Test() {
        val splitList = listOfValues.split(6)
        val expected = listOf(listOf(1, 2), listOf(3, 4), listOf(5, 6), listOf(7, 8), listOf(9), listOf(10))
        assertEquals(expected, splitList)
    }

    @Test
    fun split7Test() {
        val splitList = listOfValues.split(7)
        val expected = listOf(listOf(1, 2), listOf(3, 4), listOf(5, 6), listOf(7), listOf(8), listOf(9), listOf(10))
        assertEquals(expected, splitList)
    }

    @Test
    fun split10Test() {
        val splitList = listOfValues.split(10)
        val expected = listOf(listOf(1), listOf(2), listOf(3), listOf(4), listOf(5), listOf(6), listOf(7), listOf(8), listOf(9), listOf(10))
        assertEquals(expected, splitList)
    }

    @Test
    fun split11Test() {
        val splitList = listOfValues.split(11)
        val expected = listOf(listOf(1), listOf(2), listOf(3), listOf(4), listOf(5), listOf(6), listOf(7), listOf(8), listOf(9), listOf(10))
        assertEquals(expected, splitList)
    }

}