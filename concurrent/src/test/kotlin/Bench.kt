import org.junit.Test
import ru.inforion.lab403.common.concurrent.collections.AppendOnlyArrayList
import ru.inforion.lab403.common.concurrent.collections.appendOnlyArrayListOf
import ru.inforion.lab403.common.concurrent.collections.toAppendOnlyArrayListOf
import java.util.*
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType
import kotlin.system.measureNanoTime
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

internal class Bench {
    @Test fun benchAdd() {
        val total = 1000
        val str = "SomeString"
        val time = measureNanoTime {
            repeat(total) {
                val array = appendOnlyArrayListOf<String>()
                repeat(1000000) {
                    array.add(str)
                }
            }
        }
        println("Mean: ${time / total / 1000_000}ms")
    }

    @OptIn(ExperimentalTime::class)
    @Test fun fromCollection() {
        val total = 1000
        val warming = 10
        val src = (0..9_999_999).toList()

        repeat(warming) {
            val array = src.toAppendOnlyArrayListOf()
        }
        val (value, time) = measureTimedValue {
            repeat(total) {
                val array = src.toAppendOnlyArrayListOf()
            }
        }
        println("Mean: ${time/1000}")
    }
}