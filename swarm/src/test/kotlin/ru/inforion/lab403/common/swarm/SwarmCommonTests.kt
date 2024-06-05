package ru.inforion.lab403.common.swarm


import org.junit.jupiter.api.Test
import ru.inforion.lab403.common.extensions.bytes
import ru.inforion.lab403.common.extensions.hexlify
import ru.inforion.lab403.common.extensions.random
import ru.inforion.lab403.common.logging.FINE
import ru.inforion.lab403.common.logging.FINEST
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.serialization.deserialize
import ru.inforion.lab403.common.serialization.serialize
import ru.inforion.lab403.common.swarm.common.Slave
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import kotlin.concurrent.thread
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue


internal class SwarmCommonTests {
    companion object {
        val log = logger()
    }

    private val size = 4  // tests depend on it and should be changed

    private fun finest() {
        Swarm.log.level = FINEST
        Slave.log.level = FINEST
    }

    private fun fine() {
        Swarm.log.level = FINE
        Slave.log.level = FINE
    }

    @Test
    fun rankTest() = threadsSwarm(size) { swarm ->
        val result = swarm.context { it }.get { rank: Int -> rank }
        assertEquals(listOf(1, 2, 3, 4), result.toList())
    }

    @Test
    fun mapContextTest() = threadsSwarm(size) { swarm ->
        data class Context(var x: Int, val snapshot: String)

        swarm.context {
            val snapshot = random.bytes(10)
            Context(0, snapshot.hexlify())
        }

        val mapResults = listOf(
            "test/ab",
            "test/cd",
            "test/dd",
            "test/ab",
            "test/cd",
            "test/dd",
            "test/ab",
            "test/cd",
            "test/dd",
            "test/ab",
            "test/cd",
            "test/dd"
        ).parallelize(swarm).mapContext { context: Context, value ->
            context.x += 1
            log.info { context.toString() }
            value.uppercase().split("/")[1].toInt(16)
        }

        assertEquals(listOf(0xAB, 0xCD, 0xDD, 0xAB, 0xCD, 0xDD, 0xAB, 0xCD, 0xDD, 0xAB, 0xCD, 0xDD), mapResults)

        val getResults = swarm.get { context: Context -> context.x }

        log.info { getResults.toString() }

        assertEquals(getResults.size, size)
        assertTrue { getResults.none { it > 11 } }
    }

    @Test
    fun map2ContextTest() = threadsSwarm(size) { swarm ->
        data class Context(var x: Int, val snapshot: String)

        swarm.context {
            val snapshot = random.bytes(10)
            Context(0, snapshot.hexlify())
        }

        val mapResults = listOf(
            "test/ab",
            "test/cd",
            "test/dd",
            "test/ab",
            "test/cd",
            "test/dd",
            "test/ab",
            "test/cd",
            "test/dd",
            "test/ab",
            "test/cd",
            "test/dd"
        ).parallelize(swarm).map2Context { context: Context, value ->
            context.x += 1
            log.info { context.toString() }
            value.uppercase().split("/")[1].toInt(16)
        }

        assertEquals(listOf(0xAB, 0xCD, 0xDD, 0xAB, 0xCD, 0xDD, 0xAB, 0xCD, 0xDD, 0xAB, 0xCD, 0xDD), mapResults)

        val getResults = swarm.get { context: Context -> context.x }

        log.info { getResults.toString() }

        assertEquals(getResults.size, size)
        assertTrue { getResults.none { it > 11 } }
    }

    @Test
    fun mapTestMany() = threadsSwarm(size) { swarm ->
        val result = listOf("test/ab", "test/cd", "test/dd").parallelize(swarm).map {
            it.uppercase().split("/")[1].toInt(16)
        }

        assertEquals(listOf(0xAB, 0xCD, 0xDD), result)
    }

    @Test
    fun mapTestSingle() = threadsSwarm(1) { swarm ->
        val result = listOf("test/ab", "test/cd", "test/dd").parallelize(swarm).map {
            it.uppercase().split("/")[1].toInt(16)
        }

        assertEquals(listOf(0xAB, 0xCD, 0xDD), result)
    }

    @Test
    fun mapTestSingleWithThread() = threadsSwarm(2) { swarm ->
        var result: Collection<Int>? = null
        thread {
            result = listOf("test/ab", "test/cd", "test/dd").parallelize(swarm).map {
                it.uppercase().split("/")[1].toInt(16)
            }
        }.join()

        assertEquals(setOf(0xAB, 0xCD, 0xDD), result!!.toSet())
    }

    @Test
    fun contextCreateTest() = threadsSwarm(size) { swarm ->
        val result = swarm
            .context { "context-$it" }
            .get { context: String -> context }
        assertEquals(listOf("context-1", "context-2", "context-3", "context-4"), result)
    }

    @Test
    fun notifyReceiveTest() = threadsSwarm(size) { swarm ->
        val no1 = swarm.addReceiveNotifier { log.info { "1-$it" } }
        val no2 = swarm.addReceiveNotifier { log.info { "2-$it" } }
        swarm.removeReceiveNotifier(no1)
        swarm.removeReceiveNotifier { log.info { "2-$it" } }
        val result = Array(100) { it }.parallelize(swarm).map { it.toString() }
        assertEquals(Array(100) { it.toString() }.toList(), result)
    }

    private class MemoryConsumption(val max: Long, val free: Long, val total: Long) {
        companion object {
            fun get() = with(Runtime.getRuntime()) {
                val max = maxMemory() / 1024 / 1024
                val free = freeMemory() / 1024 / 1024
                val total = totalMemory() / 1024 / 1024
                MemoryConsumption(max, free, total)
            }
        }

        override fun toString() = "max=${max}MB free=${free}MB total=${total}MB"
    }

    @Test
    fun objectGzipStream() {
        val string = "Some insignificant string"

        val baos = ByteArrayOutputStream()
        val gos = GZIPOutputStream(baos)
        val oos = ObjectOutputStream(gos)

        oos.writeUnshared(string)

        gos.finish()

        baos.write(0x69)

        val array = baos.toByteArray()

        log.info { array.hexlify() }

        val bais = array.inputStream()

        log.info { "bais.available=${bais.available()}" }

        val gis = GZIPInputStream(bais)
        val ois = ObjectInputStream(gis)

        val result = ois.readUnshared() as String

        log.config { "bais.available=${bais.available()} due to GZIP = 0 but should be 1 (marker)" }

        assertEquals(string, result)
    }

    @Test
    fun serializationGzipStream() {
        val string = "Some insignificant string-".repeat(1000)

        val array = string.serialize( true)
        log.info { array.hexlify() }
        val result = array.deserialize<String>(true)

        assertEquals(string, result)
    }

    private fun largeHeapObjectRun(array: ByteArray) {
        System.gc()

        threadsSwarm(size, true) { swarm ->
            val start = System.currentTimeMillis()
            swarm.context { array }
            val time = System.currentTimeMillis() - start
            log.info { "Finish context compress, time = $time" }

            swarm.eachContext { context: ByteArray -> context.sum() }

            val mc2 = MemoryConsumption.get()
            log.info { mc2.toString() }
        }

        System.gc()

        threadsSwarm(size, false) { swarm ->
            val start = System.currentTimeMillis()
            swarm.context { array }
            val time = System.currentTimeMillis() - start
            log.info { "Finish context no compress, time = $time" }

            swarm.eachContext { context: ByteArray -> context.sum() }

            val mc2 = MemoryConsumption.get()
            log.info { mc2.toString() }
        }
    }

    @Test
    fun largeZeroHeapObjectTest() {
        log.config { "Staring zero object..." }
        val mc0 = MemoryConsumption.get()
        log.info { mc0.toString() }

        val array = ByteArray(0x1000_0000 ushr 2)

        val mc1 = MemoryConsumption.get()
        log.info { mc1.toString() }

        largeHeapObjectRun(array)
    }

    @Test
    fun largeRandomHeapObjectTest() {
        log.config { "Staring random object..." }
        val mc0 = MemoryConsumption.get()
        log.info { mc0.toString() }

        val array = random.bytes(0x1000_0000 ushr 2)

        val mc1 = MemoryConsumption.get()
        log.info { mc1.toString() }

        largeHeapObjectRun(array)
    }

    @Test
    fun exceptionSlaveTest() {
        assertFails {
            log.warning { "Here may be exception... it's normal" }
            threadsSwarm(size) { swarm ->
                swarm.context { require(it != 1) { "Won't work on first slave node!" } }
            }
        }
    }

    @Test
    fun exceptionMasterTest() {
        finest()
        assertFails {
            log.warning { "Here may be exception... it's normal" }
            threadsSwarm(size) { swarm ->
                swarm.context {
                    require(it != 0) { "Won't work on master node!" }
                }
                error("I said won't work on master node!")
            }
        }
        fine()
    }

    @Test
    fun exceptionFreeTest() {
        finest()
        threadsSwarm(size) {

        }
        fine()
    }
}
