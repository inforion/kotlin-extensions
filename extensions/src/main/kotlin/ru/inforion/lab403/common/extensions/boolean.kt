@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.extensions

import unsigned.types.*
import unsigned.literal.*

inline val Boolean.byte: Byte get() = if (this) 1 else 0
inline val Boolean.short: Short get() = if (this) 1 else 0
inline val Boolean.int: Int get() = if (this) 1 else 0
inline val Boolean.long: Long get() = if (this) 1 else 0

inline val Boolean.ubyte: UByte get() = if (this) Ib else Ob
inline val Boolean.ushort: UShort get() = if (this) Is else Os
inline val Boolean.uint: UInt get() = if (this) I else O
inline val Boolean.ulong: ULong get() = if (this) Il else Ol

fun convertBooleanArrayToNumber(range: IntRange = 0..31, converter: (Int) -> Boolean) =
    range.fold(0) { result, k -> result.insert(converter(k).int, k) }

fun convertNumberToBooleanArray(value: Long, range: IntRange = 0..31, converter: (Int, Boolean) -> Unit) =
    range.forEach { converter(it, value[it].truth) }