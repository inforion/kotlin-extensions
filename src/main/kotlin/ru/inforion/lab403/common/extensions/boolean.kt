@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions

/**
 * Created by Alexei Gladkikh on 26/03/17.
 */
inline fun Byte.toBool(): Boolean = this.toInt() != 0
inline fun Short.toBool(): Boolean = this.toInt() != 0
inline fun Int.toBool(): Boolean = this != 0
inline fun Long.toBool(): Boolean = this != 0L

inline fun Boolean.toByte(): Byte = if (this) 1 else 0
inline fun Boolean.toShort(): Short = if (this) 1 else 0
inline fun Boolean.toInt(): Int = if (this) 1 else 0
inline fun Boolean.toLong(): Long = if (this) 1L else 0L

inline val Boolean.asByte get() = toByte()
inline val Boolean.asShort get() = toShort()
inline val Boolean.asInt get() = toInt()
inline val Boolean.asLong get() = toLong()

fun convertBooleanArrayToNumber(range: IntRange = 0..31, converter: (Int) -> Boolean): Long {
    var result = 0L
    for (k in range)
        result = result.insert(converter(k).toInt(), k)
    return result
}

fun convertNumberToBooleanArray(value: Long, range: IntRange = 0..31, converter: (Int, Boolean) -> Unit) =
    range.forEach { converter(it, value[it].toBool()) }