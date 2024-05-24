package ru.inforion.lab403.common.logging.jmh

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import ru.inforion.lab403.common.logging.logger.Logger
import ru.inforion.lab403.common.logging.storage.LoggerStorage
import java.util.concurrent.TimeUnit


@BenchmarkMode(Mode.AverageTime)
@Fork(value = 16, warmups = 2)
@Warmup(iterations = 200, time = 500, timeUnit = TimeUnit.NANOSECONDS)
@Measurement(iterations = 10000, time = 600, timeUnit = TimeUnit.NANOSECONDS)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class LoggerNoCacheTest {
    @State(Scope.Benchmark)
    open class LoggerState {
        val loggers = listOf(
            Logger.create("a.b.c.d.e.f", false),
            Logger.create("a.b.c.d.h.i", false),
            Logger.create("a.b.c.x", false),
            Logger.create("a.y", false),
        )

        @Setup(Level.Invocation)
        fun setupInvocation() {
            loggers.forEach { it.invalidate() }
        }

        @Setup(Level.Trial)
        fun setupIteration(blackhole: Blackhole) {
            setupPublishersJmh(blackhole)
            require(LoggerStorage.getAllInfo().values.size == 5) {
                "Publishers duplicated ${LoggerStorage.getAllInfo().values.size}"
            }
        }
    }

    @Benchmark
    fun noCacheLoggerTest(state: LoggerState) {
        state.loggers.forEach {
            it.info {
                "My message 1"
            }
        }
    }
}
