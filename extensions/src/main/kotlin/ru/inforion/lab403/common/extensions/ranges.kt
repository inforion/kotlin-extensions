@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.extensions

import java.io.Serializable
import kotlin.math.max
import kotlin.math.min

val ULongRange.length get() = maxOf(last, first) - minOf(last, first) + 1u
val UIntRange.length get() = maxOf(last, first) - minOf(last, first) + 1u
val LongRange.length get() = maxOf(last, first) - minOf(last, first) + 1
val IntRange.length get() = maxOf(last, first) - minOf(last, first) + 1

val ULongRange.lengthExclusively get() = maxOf(last, first) - minOf(last, first)
val UIntRange.lengthExclusively get() = maxOf(last, first) - minOf(last, first)
val LongRange.lengthExclusively get() = maxOf(last, first) - minOf(last, first)
val IntRange.lengthExclusively get() = maxOf(last, first) - minOf(last, first)

infix fun Long.between(range: LongRange) = this > range.first && this < range.last
infix fun Int.between(range: IntRange) = this > range.first && this < range.last
infix fun Short.between(range: IntRange) = this > range.first && this < range.last
infix fun Byte.between(range: IntRange) = this > range.first && this < range.last

infix fun Long.span(range: LongRange) = this >= range.first && this < range.last
infix fun Int.span(range: IntRange) = this >= range.first && this < range.last
infix fun Short.span(range: IntRange) = this >= range.first && this < range.last
infix fun Byte.span(range: IntRange) = this >= range.first && this < range.last

infix fun LongRange.merge(other: LongRange) = min(first, other.first)..max(last, other.last)
infix fun LongRange.intersect(other: LongRange) = max(first, other.first)..min(last, other.last)
infix fun LongRange.isIntersect(other: LongRange) = min(last, other.last) >= max(first, other.first)
infix fun LongRange.isNotIntersect(other: LongRange) = !isIntersect(other)

infix fun IntRange.merge(other: IntRange) = min(first, other.first)..max(last, other.last)
infix fun IntRange.intersect(other: IntRange) = max(first, other.first)..min(last, other.last)
infix fun IntRange.isIntersect(other: IntRange) = min(last, other.last) >= max(first, other.first)
infix fun IntRange.isNotIntersect(other: IntRange) = !isIntersect(other)

infix fun UIntRange.merge(other: UIntRange) = min(first, other.first)..max(last, other.last)
infix fun UIntRange.intersect(other: UIntRange) = max(first, other.first)..min(last, other.last)
infix fun UIntRange.isIntersect(other: UIntRange) = min(last, other.last) >= max(first, other.first)
infix fun UIntRange.isNotIntersect(other: UIntRange) = !isIntersect(other)

infix fun ULongRange.merge(other: ULongRange) = min(first, other.first)..max(last, other.last)
infix fun ULongRange.intersect(other: ULongRange) = max(first, other.first)..min(last, other.last)
infix fun ULongRange.isIntersect(other: ULongRange) = min(last, other.last) >= max(first, other.first)
infix fun ULongRange.isNotIntersect(other: ULongRange) = !isIntersect(other)

inline val ULongRange.hex8 get() = "${first.hex8}..${last.hex8}"
inline val ULongRange.hex4 get() = "${first.hex4}..${last.hex4}"
inline val ULongRange.hex2 get() = "${first.hex2}..${last.hex2}"

inline val LongRange.hex8 get() = "${first.hex8}..${last.hex8}"
inline val LongRange.hex4 get() = "${first.hex4}..${last.hex4}"
inline val LongRange.hex2 get() = "${first.hex2}..${last.hex2}"

inline val UIntRange.hex8 get() = "${first.hex8}..${last.hex8}"
inline val UIntRange.hex4 get() = "${first.hex4}..${last.hex4}"
inline val UIntRange.hex2 get() = "${first.hex2}..${last.hex2}"

inline val IntRange.hex8 get() = "${first.hex8}..${last.hex8}"
inline val IntRange.hex4 get() = "${first.hex4}..${last.hex4}"
inline val IntRange.hex2 get() = "${first.hex2}..${last.hex2}"

data class Range<T : Number>(val first: T, val last: T) : Serializable

// TODO: uncomment when JB makes ULong and UInt numbers
//inline fun ULongRange.toSerializable() = Range(first, last)
//inline fun UIntRange.toSerializable() = Range(first, last)

inline fun LongRange.toSerializable() = Range(first, last)
inline fun IntRange.toSerializable() = Range(first, last)