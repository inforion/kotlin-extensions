import org.junit.jupiter.api.Test
import ru.inforion.lab403.common.concurrent.collections.appendOnlyULongArrayOf
import ru.inforion.lab403.common.extensions.ulong_z
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

internal class UlongConcurrentTest {
    @Test fun concurrentAddTest() {
        val array = appendOnlyULongArrayOf()
        val executor = Executors.newFixedThreadPool(5)
        for (i in 0..9) {
            val worker = Runnable {
                (0..9).forEach { array.add(10uL * i.ulong_z + it.ulong_z) }
            }
            executor.execute(worker)
        }
        executor.shutdown()
        executor.awaitTermination(20, TimeUnit.SECONDS)
        assertEquals(100, array.size)
        assertEquals((0uL..99uL).toSet(), array.toSet())
    }

    @Test fun concurrentRead() {
        val array = appendOnlyULongArrayOf()
        val executor = Executors.newFixedThreadPool(10)
        for (i in 0..99) {
            val writeWorker = Runnable {
                (0..99).forEach { array.add(i.ulong_z * 100uL + it.ulong_z + 1uL) }
            }
            val readWorker = Runnable {
                val list = array as List<ULong>
                if (list.size >= 2){
                    assert(list[list.size - 1] != 0uL && list[list.size - 2] != 0uL)
                }
            }
            executor.execute(writeWorker)
            executor.execute(readWorker)
        }
        executor.shutdown()
        executor.awaitTermination(10, TimeUnit.SECONDS)
    }
}