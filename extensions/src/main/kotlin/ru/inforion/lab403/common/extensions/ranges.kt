@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.extensions

import unsigned.types.*
import unsigned.ranges.*
import java.io.Serializable
import kotlin.math.max
import kotlin.math.min

inline val ULongRange.length get() = last - first + 1u
inline val LongRange.length get() = last - first + 1
inline val IntRange.length get() = last - first + 1

inline infix fun Long.between(range: LongRange) = this > range.first && this < range.last
inline infix fun Int.between(range: IntRange) = this > range.first && this < range.last
inline infix fun Short.between(range: IntRange) = this > range.first && this < range.last
inline infix fun Byte.between(range: IntRange) = this > range.first && this < range.last

inline infix fun Long.span(range: LongRange) = this >= range.first && this < range.last
inline infix fun Int.span(range: IntRange) = this >= range.first && this < range.last
inline infix fun Short.span(range: IntRange) = this >= range.first && this < range.last
inline infix fun Byte.span(range: IntRange) = this >= range.first && this < range.last

inline infix fun LongRange.merge(other: LongRange) = min(first, other.first)..max(last, other.last)
inline infix fun LongRange.intersect(other: LongRange) = max(first, other.first)..min(last, other.last)
inline infix fun LongRange.isIntersect(other: LongRange) = min(last, other.last) >= max(first, other.first)
inline infix fun LongRange.isNotIntersect(other: LongRange) = !isIntersect(other)

inline infix fun IntRange.merge(other: IntRange) = min(first, other.first)..max(last, other.last)
inline infix fun IntRange.intersect(other: IntRange) = max(first, other.first)..min(last, other.last)
inline infix fun IntRange.isIntersect(other: IntRange) = min(last, other.last) >= max(first, other.first)
inline infix fun IntRange.isNotIntersect(other: IntRange) = !isIntersect(other)

inline infix fun UIntRange.merge(other: UIntRange) = min(first, other.first)..max(last, other.last)
inline infix fun UIntRange.intersect(other: UIntRange) = max(first, other.first)..min(last, other.last)
inline infix fun UIntRange.isIntersect(other: UIntRange) = min(last, other.last) >= max(first, other.first)
inline infix fun UIntRange.isNotIntersect(other: UIntRange) = !isIntersect(other)

inline infix fun ULongRange.merge(other: ULongRange) = min(first, other.first)..max(last, other.last)
inline infix fun ULongRange.intersect(other: ULongRange) = max(first, other.first)..min(last, other.last)
inline infix fun ULongRange.isIntersect(other: ULongRange) = min(last, other.last) >= max(first, other.first)
inline infix fun ULongRange.isNotIntersect(other: ULongRange) = !isIntersect(other)

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

data class Range<T: Number>(val first: T, val last: T): Serializable

// TODO: uncomment when JB makes ULong and UInt numbers
//inline fun ULongRange.toSerializable() = Range(first, last)
//inline fun UIntRange.toSerializable() = Range(first, last)

inline fun LongRange.toSerializable() = Range(first, last)
inline fun IntRange.toSerializable() = Range(first, last)