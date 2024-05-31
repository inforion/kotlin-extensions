package ru.inforion.lab403.common.logging.jmh

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import ru.inforion.lab403.common.logging.logger.Logger
import ru.inforion.lab403.common.logging.publishers.setupPublishers
import ru.inforion.lab403.common.logging.storage.LoggerStorage
import java.util.concurrent.TimeUnit


@BenchmarkMode(Mode.AverageTime)
@Fork(value = 16, warmups = 2)
@Warmup(iterations = 5000, time = 500, timeUnit = TimeUnit.NANOSECONDS)
@Measurement(iterations = 15000, time = 600, timeUnit = TimeUnit.NANOSECONDS)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class CacheBenchMarkTest {
    @State(Scope.Benchmark)
    open class LoggerState {
        val loggers = listOf(
            Logger.create("a.b.c.d.e.f"),
            Logger.create("a.b.c.d.h.i"),
            Logger.create("a.b.c.x"),
            Logger.create("a.y"),
        )

        @Setup(Level.Trial)
        fun setupIteration(blackhole: Blackhole) {
            setupPublishersJmh(blackhole)
            require(LoggerStorage.getAllInfo().values.size == 5) {
                "Publishers duplicated ${LoggerStorage.getAllInfo().values.size}"
            }
        }
    }

    @State(Scope.Benchmark)
    open class LoggerInformativeState {
        val loggers = listOf(
            Logger.create("a.b.c.d.e.f"),
            Logger.create("a.b.c.d.h.i"),
            Logger.create("a.b.c.x"),
            Logger.create("a.y"),
        )

        @Setup(Level.Trial)
        fun setupIteration(blackhole: Blackhole) {
            setupPublishers { JmhInformativePublisher(it, blackhole) }
            require(LoggerStorage.getAllInfo().values.size == 5) {
                "Publishers duplicated ${LoggerStorage.getAllInfo().values.size}"
            }
        }
    }

    @Benchmark
    fun withCacheLoggerTest(state: LoggerState) {
        state.loggers.forEach {
            it.info {
                "My message ${it.name}"
            }
        }
    }

    @Benchmark
    fun withCacheLoggerInformativeTest(state: LoggerInformativeState) {
        state.loggers.forEach {
            it.info {
                "My message ${it.name}"
            }
        }
    }
}
