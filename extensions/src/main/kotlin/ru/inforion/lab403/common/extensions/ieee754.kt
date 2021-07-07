@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.extensions

import java.lang.Double.doubleToRawLongBits
import java.lang.Double.longBitsToDouble
import java.lang.Float.floatToRawIntBits
import java.lang.Float.intBitsToFloat

inline fun Float.ieee754AsSigned(): Int = floatToRawIntBits(this)

inline fun Float.ieee754AsUnsigned(): UInt = ieee754AsSigned().uint

inline fun Double.ieee754AsSigned(): Long = doubleToRawLongBits(this)

inline fun Double.ieee754AsUnsigned(): ULong = ieee754AsSigned().ulong

inline fun Int.ieee754(): Float = intBitsToFloat(this)

inline fun UInt.ieee754(): Float = intBitsToFloat(int)

inline fun Long.ieee754(): Double = longBitsToDouble(this)

inline fun ULong.ieee754(): Double = longBitsToDouble(long)

