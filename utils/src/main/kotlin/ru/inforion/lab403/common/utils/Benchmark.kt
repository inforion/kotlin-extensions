package ru.inforion.lab403.common.utils

import kotlin.time.Duration
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

/**
 * Benchmarks given lambda, returns the output in Us
 */
class Benchmark(val total: Int = 10_000_000, val times: Int = 10, val warming: Int = 10_000) {
    private fun accumulate(previousUs: Long, sample: Long, count: Long) =
        if (count == 0L) {
            sample
        } else {
            ((count - 1) * previousUs + sample) / count
        }

    @OptIn(ExperimentalTime::class)
    fun bench(runnable: () -> Any): Duration {
        repeat(warming) {
            run(runnable)
        }

        var timeUs = 0L;
        var count = 0L;
        repeat(times) {
            val (_, time) = measureTimedValue {
                repeat(total) {
                    run(runnable)
                }
            }

            timeUs = accumulate(timeUs, time.inWholeMicroseconds, count)
            count += 1;
        }

        return timeUs.microseconds
    }
}
