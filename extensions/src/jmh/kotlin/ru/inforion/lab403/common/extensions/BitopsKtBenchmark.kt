@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

@ExperimentalUnsignedTypes
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5)
@Measurement(iterations = 5, batchSize = 10)
@State(Scope.Benchmark)
@Fork(value = 2)
open class BitopsKtBenchmark {
    val x: ULong = 0x0400_0000uL

    inline infix fun ULong.signext2(n: Int): ULong {
        if (n < UInt.SIZE_BITS) {
            val shift = UInt.SIZE_BITS - 1 - n
            return ((this.int shl shift).long_s ashr shift).ulong
        } else TODO()
    }

    @Benchmark
    fun signext1Test1(blackhole: Blackhole) {
        blackhole.consume(x signext 27)
    }

    @Benchmark
    fun signext1Test2(blackhole: Blackhole) {
        blackhole.consume(x signext 26)
    }

    @Benchmark
    fun signext2Test1(blackhole: Blackhole) {
        blackhole.consume(x signext2 27)
    }

    @Benchmark
    fun signext2Test2(blackhole: Blackhole) {
        blackhole.consume(x signext2 26)
    }
}