package ru.inforion.lab403.common.extensions

import org.junit.Test
import org.junit.Assert.*
import ru.inforion.lab403.common.logging.logger

class RandomTest {

    val log = logger()

    private val tolerance = 0.01
    private val collectionSize = 100000

    private data class Item(val id: Int, val weight: Double)
    private val x = arrayListOf(Item(3, 0.1), Item(4, 0.45), Item(0, 0.15), Item(1, 0.25), Item(2, 0.05))
    private val q = Array(collectionSize) { x.random { it.weight } }
    private val pq = x.map { item -> q.count { it.id == item.id }.toDouble() / q.count() }

    private val z = Array(collectionSize) { x.random() }
    private val pz = x.map { item -> z.count { it.id == item.id }.toDouble() / z.count() }

    private val y = Array(collectionSize) { x.random { 0.0 } }
    private val py = x.map { item -> y.count { it.id == item.id }.toDouble() / z.count() }

    @Test fun choiceBasicTest() = assertEquals(x.sumByDouble { it.weight }, 1.0, tolerance)

    @Test fun choiceAccumTest() = assertEquals(pq.sum(), 1.0, tolerance)

    @Test fun choiceWeightedEachTest() = x.zip(pq).forEach { assertEquals(it.first.weight, it.second, tolerance) }

    @Test fun choiceUniform1EachTest() {
        val w = 1.0 / x.size
        x.zip(pz).forEach { assertEquals(w, it.second, tolerance) }
    }

    @Test fun choiceUniform0EachTest() {
        val w = 1.0 / x.size
        x.zip(py).forEach { assertEquals(w, it.second, tolerance) }
    }

    fun error(actual: Int, expected: Int) = (actual - expected) / expected.toDouble()

    fun assertHistogram(data: ByteArray, size: Int) {
        val histogram = math.histogram(data)

        // check that all values generated from 0 .. 255
        val values = histogram.map { it.value.asUInt }.toSet()
        val expected = collect(256).toSet()
        assertEquals(expected, values)

        // check uniformity
        val bad = histogram.sortedBy { it.value.asUInt }.find {
            log.fine { "0x${it.value.hex2} -> ${it.count}" }
            error(it.count, size) > 0.01
        }

        assert(bad == null) { "Found bad bin: $bad -> error = ${error(bad!!.count, size)}" }
    }

    @Test
    fun randbytes() = assertHistogram(random.bytes(100000), 100000)

    @Test
    fun success() {
        val prob = 0.69
        val total = 1_000_000
        val data = Array(total) { random.success(prob).asInt }
        val result = data.count { it == 1 } / total.toDouble()
        val error = Math.abs(result - prob)
        println("Positive success = $result, error = $error")
        assert(error < 0.01)
    }
}