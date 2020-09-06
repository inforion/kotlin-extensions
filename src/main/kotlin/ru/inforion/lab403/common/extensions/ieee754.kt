package ru.inforion.lab403.common.extensions

import java.lang.Double.doubleToRawLongBits
import java.lang.Double.longBitsToDouble
import java.lang.Float.floatToRawIntBits
import java.lang.Float.intBitsToFloat

/**
 * Created by Alexei Gladkikh on 08/02/17.
 */
fun Float.ieee754(): Int = floatToRawIntBits(this)
fun Double.ieee754(): Long = doubleToRawLongBits(this)
fun Int.ieee754(): Float = intBitsToFloat(this)
fun Long.ieee754(): Double = longBitsToDouble(this)