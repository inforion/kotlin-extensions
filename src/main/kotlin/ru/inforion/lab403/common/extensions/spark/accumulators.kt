@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.extensions.spark

import org.apache.spark.util.LongAccumulator
import ru.inforion.lab403.common.extensions.int

val LongAccumulator.valueAsInt get() = value().int

fun LongAccumulator.getAndAdd(value: Long): Long {
    val previous = value()
    add(value)
    return previous
}
