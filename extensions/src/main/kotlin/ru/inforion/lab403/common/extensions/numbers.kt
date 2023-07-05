@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions

operator fun Int.times(string: String) = string.repeat(this)

// https://stackoverflow.com/questions/7446710/how-to-round-up-integer-division-and-have-int-result-in-java
inline infix fun Int.ceil(divisor: Int) = this / divisor + if (this % divisor == 0) 0 else 1
inline infix fun UInt.ceil(divisor: UInt) = this / divisor + if (this % divisor == 0u) 0u else 1u

inline infix fun Long.ceil(divisor: Long) = this / divisor + if (this % divisor == 0L) 0 else 1
inline infix fun ULong.ceil(divisor: ULong) = this / divisor + if (this % divisor == 0uL) 0uL else 1uL

inline infix fun Int.floor(divisor: Int) = this / divisor
inline infix fun UInt.floor(divisor: UInt) = this / divisor

inline infix fun Long.floor(divisor: Long) = this / divisor
inline infix fun ULong.floor(divisor: ULong) = this / divisor
