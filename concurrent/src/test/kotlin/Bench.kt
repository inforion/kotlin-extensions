import org.junit.Test
import ru.inforion.lab403.common.concurrent.collections.AppendOnlyArrayList
import ru.inforion.lab403.common.concurrent.collections.appendOnlyArrayListOf
import ru.inforion.lab403.common.concurrent.collections.toAppendOnlyArrayListOf
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType
import kotlin.system.measureNanoTime
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

internal class Bench {
    @Test fun benchConcurrentAdd() {
        val str = "SomeString"
        bench {
            val executor = Executors.newFixedThreadPool(2)
            val array = appendOnlyArrayListOf<String>()
            val worker = Runnable { repeat(500000) { array.add(str) } }
            executor.execute(worker)
            executor.execute(worker)
            executor.shutdown()
            executor.awaitTermination(20, TimeUnit.SECONDS)
        }
    }
    
    @Test fun benchAdd() {
        val str = "SomeString"
        bench {
            val array = arrayListOf<String>()
            repeat(1000000) { array.add(str) }
        }
    }

    @OptIn(ExperimentalTime::class)
    @Test fun fromCollection() {
        val src = (0..9_999_999).toList()
        bench { src.toAppendOnlyArrayListOf() }
    }

    @OptIn(ExperimentalTime::class)
    fun bench(total: Int = 1000, warming: Int = 10, runnable: () -> Any) {
        repeat(warming) {
            run(runnable)
        }
        val (value, time) = measureTimedValue {
            repeat(total) {
                run(runnable)
            }
        }
        println("Mean: ${time/1000}")
    }
}