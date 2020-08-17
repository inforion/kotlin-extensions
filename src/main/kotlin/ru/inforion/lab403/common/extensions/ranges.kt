package ru.inforion.lab403.common.extensions

import kotlin.math.max
import kotlin.math.min

val LongRange.length get() = last - first + 1
val IntRange.length get() = last - first + 1

infix fun Long.between(range: LongRange) = this > range.first && this < range.last
infix fun Int.between(range: IntRange) = this > range.first && this < range.last
infix fun Short.between(range: IntRange) = this > range.first && this < range.last
infix fun Byte.between(range: IntRange) = this > range.first && this < range.last

infix fun Long.span(range: LongRange) = this >= range.first && this < range.last
infix fun Int.span(range: IntRange) = this >= range.first && this < range.last
infix fun Short.span(range: IntRange) = this >= range.first && this < range.last
infix fun Byte.span(range: IntRange) = this >= range.first && this < range.last

infix fun Long.inside(range: LongRange) = this >= range.first && this <= range.last
infix fun Int.inside(range: IntRange) = this >= range.first && this <= range.last
infix fun Short.inside(range: IntRange) = this >= range.first && this <= range.last
infix fun Byte.inside(range: IntRange) = this >= range.first && this <= range.last

infix fun Long.outside(range: LongRange) = this < range.first || this > range.last
infix fun Int.outside(range: IntRange) = this < range.first || this > range.last
infix fun Short.outside(range: IntRange) = this < range.first || this > range.last
infix fun Byte.outside(range: IntRange) = this < range.first || this > range.last

infix fun LongRange.merge(other: LongRange) = min(first, other.first)..max(last, other.last)
infix fun LongRange.intersect(other: LongRange) = max(first, other.first)..min(last, other.last)
infix fun LongRange.isIntersect(other: LongRange) = intersect(other).length > 0
infix fun LongRange.isNotIntersect(other: LongRange) = !isIntersect(other)

infix fun IntRange.merge(other: IntRange) = min(first, other.first)..max(last, other.last)
infix fun IntRange.intersect(other: IntRange) = max(first, other.first)..min(last, other.last)
infix fun IntRange.isIntersect(other: IntRange) = intersect(other).length > 0
infix fun IntRange.isNotIntersect(other: IntRange) = !isIntersect(other)

val LongRange.hex8 get() = "${first.hex8}..${last.hex8}"
val LongRange.hex4 get() = "${first.hex4}..${last.hex4}"
val LongRange.hex2 get() = "${first.hex2}..${last.hex2}"

val IntRange.hex8 get() = "${first.hex8}..${last.hex8}"
val IntRange.hex4 get() = "${first.hex4}..${last.hex4}"
val IntRange.hex2 get() = "${first.hex2}..${last.hex2}"