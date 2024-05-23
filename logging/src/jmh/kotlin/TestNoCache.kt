package ru.inforion.lab403.common.logging.jmh

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.annotations.State
import ru.inforion.lab403.common.logging.INFO
import ru.inforion.lab403.common.logging.logger
import java.util.concurrent.TimeUnit

@Suppress("NOTHING_TO_INLINE")
@BenchmarkMode(Mode.AverageTime)
@Fork(value = 16, warmups = 2)
@Warmup(iterations = 200, time = 500, timeUnit = TimeUnit.NANOSECONDS)
@Measurement(iterations = 10000, time = 600, timeUnit = TimeUnit.NANOSECONDS)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class LoggerNoCacheTest(){

    @State(Scope.Benchmark)
    companion object LoggerState{
        val logger = logger(INFO)

        val publisherFirst = TestPublisher("Test1").also {
            CacheBenchMarkTest.logger.addPublisher(it)
        }

        @Setup(Level.Invocation)
        fun invalidateLogger(){
            logger.invalidate()
        }
    }

    @Benchmark
    fun noCacheLoggerTest(){
        logger.info {
            "My message"
        }
    }

}
