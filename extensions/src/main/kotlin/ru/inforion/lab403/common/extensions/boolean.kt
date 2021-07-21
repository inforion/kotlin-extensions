@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.extensions

import unsigned.types.*
import unsigned.literal.*

inline val Boolean.byte: Byte get() = if (this) 1 else 0
inline val Boolean.short: Short get() = if (this) 1 else 0
inline val Boolean.int: Int get() = if (this) 1 else 0
inline val Boolean.long: Long get() = if (this) 1 else 0

inline val Boolean.ubyte: UByte get() = if (this) 1[b] else 0[b]
inline val Boolean.ushort: UShort get() = if (this) 1[s] else 0[s]
inline val Boolean.uint: UInt get() = if (this) 1[u] else 0[u]
inline val Boolean.ulong: ULong get() = if (this) 1[ul] else 0[ul]

fun convertBooleanArrayToNumber(range: IntRange = 0..31, converter: (Int) -> Boolean) =
    range.fold(0) { result, k -> result.insert(converter(k).int, k) }

fun convertNumberToBooleanArray(value: Long, range: IntRange = 0..31, converter: (Int, Boolean) -> Unit) =
    range.forEach { converter(it, value[it].truth) }