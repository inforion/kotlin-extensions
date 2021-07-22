@file:Suppress("NOTHING_TO_INLINE", "unused")

package unsigned.math

import unsigned.types.*

inline fun max(a: UInt, b: UInt) = if (a >= b) a else b
inline fun max(a: ULong, b: ULong) = if (a >= b) a else b
inline fun max(a: UByte, b: UByte) = if (a >= b) a else b
inline fun max(a: UShort, b: UShort) = if (a >= b) a else b
inline fun max(a: UInt, b: UInt, c: UInt) = max(a, max(b, c))
inline fun max(a: ULong, b: ULong, c: ULong) = max(a, max(b, c))
inline fun max(a: UByte, b: UByte, c: UByte) = max(a, max(b, c))
inline fun max(a: UShort, b: UShort, c: UShort) = max(a, max(b, c))

inline fun min(a: UInt, b: UInt) = if (a <= b) a else b
inline fun min(a: ULong, b: ULong) = if (a <= b) a else b
inline fun min(a: UByte, b: UByte) = if (a <= b) a else b
inline fun min(a: UShort, b: UShort) = if (a <= b) a else b
inline fun min(a: UInt, b: UInt, c: UInt) = min(a, min(b, c))
inline fun min(a: ULong, b: ULong, c: ULong) = min(a, min(b, c))
inline fun min(a: UByte, b: UByte, c: UByte) = min(a, min(b, c))
inline fun min(a: UShort, b: UShort, c: UShort) = min(a, min(b, c))

