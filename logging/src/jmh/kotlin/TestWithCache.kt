package ru.inforion.lab403.common.logging.jmh

import org.openjdk.jmh.annotations.*
import ru.inforion.lab403.common.logging.INFO
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.logging.storage.LoggerStorage
import java.util.concurrent.TimeUnit

@Suppress("NOTHING_TO_INLINE")
@BenchmarkMode(Mode.AverageTime)
@Fork(value = 16, warmups = 2)
@Warmup(iterations = 200, time = 500, timeUnit = TimeUnit.NANOSECONDS)
@Measurement(iterations = 10000, time = 600, timeUnit = TimeUnit.NANOSECONDS)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class CacheBenchMarkTest {

    @State(Scope.Benchmark)
    companion object{
        val logger = logger(INFO)
        val publisherFirst = TestPublisher("Test1").also {
            logger.addPublisher(it)
        }
        val publisherSecond = TestPublisherSecond("Test2").also {
            LoggerStorage.addPublisher(it)
        }
    }

    @Benchmark
    fun testWithCache() {
        logger.info {
            "My message"
        }
    }

}
