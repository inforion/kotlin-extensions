@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.extensions


inline val Boolean.byte: Byte get() = if (this) 1 else 0
inline val Boolean.short: Short get() = if (this) 1 else 0
inline val Boolean.int: Int get() = if (this) 1 else 0
inline val Boolean.long: Long get() = if (this) 1 else 0

inline val Boolean.ubyte: UByte get() = if (this) 1u else 0u
inline val Boolean.ushort: UShort get() = if (this) 1u else 0u
inline val Boolean.uint: UInt get() = if (this) 1u else 0u
inline val Boolean.ulong: ULong get() = if (this) 1u else 0u

fun convertBooleanArrayToNumber(range: IntRange = 0..31, converter: (Int) -> Boolean) =
    range.fold(0) { result, k -> result.insert(converter(k).int, k) }

fun convertNumberToBooleanArray(value: Long, range: IntRange = 0..31, converter: (Int, Boolean) -> Unit) =
    range.forEach { converter(it, value[it].truth) }