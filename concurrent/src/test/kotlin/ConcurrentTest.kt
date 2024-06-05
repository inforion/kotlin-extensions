import org.junit.jupiter.api.Test
import ru.inforion.lab403.common.concurrent.collections.appendOnlyArrayListOf
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

internal class ConcurrentTest {
    @Test fun concurrentAddTest() {
        val array = appendOnlyArrayListOf<Int>()
        val executor = Executors.newFixedThreadPool(5)
        for (i in 0..9) {
            val worker = Runnable {
                (0..9).forEach { array.add(i * 10 + it) }
            }
            executor.execute(worker)
        }
        executor.shutdown()
        executor.awaitTermination(20, TimeUnit.SECONDS)
        assertEquals(100, array.size)
        assertEquals((0..99).toSet(), array.toSet())
    }

    @Test fun concurrentRead() {
        val array = appendOnlyArrayListOf<Int>()
        val executor = Executors.newFixedThreadPool(10)
        for (i in 0..99) {
            val writeWorker = Runnable {
                (0..99).forEach { array.add(i * 100 + it) }
            }
            val readWorker = Runnable {
                val list = array as List<Int?>
                if (list.size >= 2){
                    assert(list[list.size - 1] != null && list[list.size - 2] != null)
                }
            }
            executor.execute(writeWorker)
            executor.execute(readWorker)
        }
        executor.shutdown()
        executor.awaitTermination(10, TimeUnit.SECONDS)
    }
}