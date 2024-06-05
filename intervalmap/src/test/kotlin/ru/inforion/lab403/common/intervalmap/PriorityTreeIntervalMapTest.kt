package ru.inforion.lab403.common.intervalmap

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


internal class PriorityTreeIntervalMapTest {

    private fun assertEquals(expected: Char, actual: Interval) = assertEquals(expected.id, actual.id)
    
    @Test
    fun mapAddTest() {
        val map = PriorityTreeIntervalMap('S', 0u, 100u)

        map.add('A', 0u, 50u)

        assertEquals('A', map[0u])
        assertEquals('A', map[1u])
        assertEquals('A', map[50u])
        assertEquals('S', map[51u])

        map.add('B', 20u, 70u)

        assertEquals('A', map[0u])
        assertEquals('A', map[1u])

        assertEquals('A', map[19u])

        assertEquals('B', map[20u])
        assertEquals('B', map[21u])

        assertEquals('B', map[49u])
        assertEquals('B', map[50u])
        assertEquals('B', map[51u])

        assertEquals('B', map[69u])
        assertEquals('B', map[70u])

        assertEquals('S', map[71u])
    }

    @Test
    fun mapAdd2Test() {
        val map = PriorityTreeIntervalMap('S', 0u, 100u)

        map.add('A', 0u, 50u)

        assertEquals('A', map[0u])
        assertEquals('A', map[50u])
        assertEquals('S', map[51u])
        assertEquals('S', map[100u])

        map.add('B', 1u, 49u)

        assertEquals('A', map[0u])
        assertEquals('B', map[1u])
        assertEquals('B', map[2u])
        assertEquals('B', map[48u])
        assertEquals('B', map[49u])
        assertEquals('A', map[50u])
        assertEquals('S', map[51u])
        assertEquals('S', map[100u])
    }

    @Test
    fun mapAdd3Test() {
        val map = PriorityTreeIntervalMap('S', 0u, 100u)

        map.add('A', 20u, 50u)

        assertEquals('S', map[0u])
        assertEquals('S', map[19u])
        assertEquals('A', map[20u])
        assertEquals('A', map[21u])
        assertEquals('A', map[50u])
        assertEquals('S', map[51u])
        assertEquals('S', map[100u])

        map.add('B', 30u, 60u)

        assertEquals('S', map[0u])
        assertEquals('S', map[19u])
        assertEquals('A', map[20u])
        assertEquals('A', map[21u])
        assertEquals('A', map[29u])
        assertEquals('B', map[30u])
        assertEquals('B', map[31u])
        assertEquals('B', map[50u])
        assertEquals('B', map[60u])
        assertEquals('S', map[61u])
        assertEquals('S', map[100u])
    }

    @Test
    fun mapAdd4Test() {
        val map = PriorityTreeIntervalMap('S', 0u, 100u)

        map.add('A', 50u, 100u)

        assertEquals('S', map[0u])
        assertEquals('S', map[49u])
        assertEquals('A', map[50u])
        assertEquals('A', map[100u])

        map.add('B', 0u, 50u)

        assertEquals('A', map[51u])
        assertEquals('A', map[100u])
        assertEquals('B', map[0u])
        assertEquals('B', map[50u])
    }

    @Test
    fun mapAddRemoveTest() {
        val map = PriorityTreeIntervalMap('S', 0u, 100u)

        map.add('A', 50u, 100u)

        assertEquals('S', map[0u])
        assertEquals('S', map[49u])
        assertEquals('A', map[50u])
        assertEquals('A', map[100u])

        map.add('B', 0u, 50u)

        assertEquals('B', map[0u])
        assertEquals('B', map[50u])
        assertEquals('A', map[51u])
        assertEquals('A', map[100u])

        map.add('C', 25u, 75u)

        assertEquals('B', map[0u])
        assertEquals('B', map[24u])
        assertEquals('C', map[25u])
        assertEquals('C', map[75u])
        assertEquals('A', map[76u])
        assertEquals('A', map[100u])

        map.add('D', 65u, 85u)

        println(map)

        assertEquals('B', map[0u])
        assertEquals('B', map[24u])
        assertEquals('C', map[25u])
        assertEquals('C', map[64u])
        assertEquals('D', map[65u])
        assertEquals('D', map[85u])
        assertEquals('A', map[86u])
        assertEquals('A', map[100u])

        map.add('E', 50u, 100u)

        assertEquals('B', map[0u])
        assertEquals('B', map[24u])
        assertEquals('C', map[25u])
        assertEquals('E', map[50u])
        assertEquals('E', map[100u])

        map.remove('E')

        println(map)

        assertEquals('B', map[0u])
        assertEquals('B', map[24u])
        assertEquals('C', map[25u])
        assertEquals('C', map[64u])
        assertEquals('D', map[65u])
        assertEquals('D', map[85u])
        assertEquals('A', map[86u])
        assertEquals('A', map[100u])
    }
}