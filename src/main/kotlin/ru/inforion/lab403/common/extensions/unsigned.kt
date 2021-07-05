@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions

inline operator fun ULong.plus(other: Int) = this + other.uint
inline operator fun ULong.plus(other: Short) = this + other.uint_s
inline operator fun ULong.plus(other: Byte) = this + other.uint_s

inline operator fun ULong.minus(other: Int) = this - other.uint
inline operator fun ULong.minus(other: Short) = this - other.uint_s
inline operator fun ULong.minus(other: Byte) = this - other.uint_s

inline operator fun UInt.plus(other: Int) = this + other.uint
inline operator fun UInt.plus(other: Short) = this + other.uint_s
inline operator fun UInt.plus(other: Byte) = this + other.uint_s

inline operator fun ULong.unaryMinus() = (-long).ulong
inline operator fun UInt.unaryMinus() = (-int).uint