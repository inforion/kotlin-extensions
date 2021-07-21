@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions

import unsigned.types.*

inline operator fun ULong.plus(other: Long) = this + other.ulong
inline operator fun ULong.plus(other: Int) = this + other.uint
inline operator fun ULong.plus(other: Short) = this + other.uint_s
inline operator fun ULong.plus(other: Byte) = this + other.uint_s

inline operator fun ULong.minus(other: Long) = this - other.ulong
inline operator fun ULong.minus(other: Int) = this - other.uint
inline operator fun ULong.minus(other: Short) = this - other.uint_s
inline operator fun ULong.minus(other: Byte) = this - other.uint_s

inline operator fun ULong.times(other: Long) = this * other.ulong
inline operator fun ULong.times(other: Int) = this * other.uint
inline operator fun ULong.times(other: Short) = this * other.uint_s
inline operator fun ULong.times(other: Byte) = this * other.uint_s

inline operator fun ULong.div(other: Long) = this / other.ulong
inline operator fun ULong.div(other: Int) = this / other.uint
inline operator fun ULong.div(other: Short) = this / other.uint_s
inline operator fun ULong.div(other: Byte) = this / other.uint_s

inline operator fun ULong.rem(other: Long) = this % other.ulong
inline operator fun ULong.rem(other: Int) = this % other.uint
inline operator fun ULong.rem(other: Short) = this % other.uint_s
inline operator fun ULong.rem(other: Byte) = this % other.uint_s

inline operator fun UInt.plus(other: Int) = this + other.uint
inline operator fun UInt.plus(other: Short) = this + other.uint_s
inline operator fun UInt.plus(other: Byte) = this + other.uint_s

inline operator fun UInt.minus(other: Int) = this - other.uint
inline operator fun UInt.minus(other: Short) = this - other.uint_s
inline operator fun UInt.minus(other: Byte) = this - other.uint_s

inline operator fun UInt.times(other: Int) = this * other.uint
inline operator fun UInt.times(other: Short) = this * other.uint_s
inline operator fun UInt.times(other: Byte) = this * other.uint_s

inline operator fun UInt.div(other: Int) = this / other.uint
inline operator fun UInt.div(other: Short) = this / other.uint_s
inline operator fun UInt.div(other: Byte) = this / other.uint_s

inline operator fun UInt.rem(other: Int) = this % other.uint
inline operator fun UInt.rem(other: Short) = this % other.uint_s
inline operator fun UInt.rem(other: Byte) = this % other.uint_s

inline operator fun ULong.unaryMinus() = (-long).ulong
inline operator fun UInt.unaryMinus() = (-int).uint