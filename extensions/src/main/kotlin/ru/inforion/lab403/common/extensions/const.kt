@file:Suppress("unused")

package ru.inforion.lab403.common.extensions

import unsigned.types.*

@Deprecated("Don't use this constant because no sense with unsigned types")
const val WRONGL = -1L
@Deprecated("Don't use this constant because no sense with unsigned types")
const val WRONGI = -1
@Deprecated("Don't use this constant because no sense with unsigned types")
const val UNDEF = -1

@Deprecated("Don't use this constant because no sense with unsigned types")
const val INT64MASK = -1L
@Deprecated("Don't use this constant because no sense with unsigned types")
const val INT32MASK = 0xFFFF_FFFF
@Deprecated("Don't use this constant because no sense with unsigned types")
const val INT16MASK = 0xFFFF
@Deprecated("Don't use this constant because no sense with unsigned types")
const val INT8MASK = 0xFF
@Deprecated("Don't use this constant because no sense with unsigned types")
const val INT1MASK = 1

val ULONG_MAX = ULong.MAX_VALUE
val ULONG_MIN = ULong.MIN_VALUE
const val ULONG_BITS = ULong.SIZE_BITS
const val ULONG_BYTES = ULong.SIZE_BYTES

const val LONG_MAX = Long.MAX_VALUE
const val LONG_MIN = Long.MIN_VALUE
const val LONG_BITS = Long.SIZE_BITS
const val LONG_BYTES = Long.SIZE_BYTES

const val INT_MAX = Int.MAX_VALUE
const val INT_MIN = Int.MIN_VALUE
const val INT_BITS = Int.SIZE_BITS
const val INT_BYTES = Int.SIZE_BYTES

val UINT_MAX = UInt.MAX_VALUE
val UINT_MIN = UInt.MIN_VALUE
const val UINT_BITS = UInt.SIZE_BITS
const val UINT_BYTES = UInt.SIZE_BYTES

const val SHORT_MAX = Short.MAX_VALUE
const val SHORT_MIN = Short.MIN_VALUE
const val SHORT_BITS = Short.SIZE_BITS
const val SHORT_BYTES = Short.SIZE_BYTES

val USHORT_MAX = UShort.MAX_VALUE
val USHORT_MIN = UShort.MIN_VALUE
const val USHORT_BITS = UShort.SIZE_BITS
const val USHORT_BYTES = UShort.SIZE_BYTES

const val BYTE_MAX = Byte.MAX_VALUE
const val BYTE_MIN = Byte.MIN_VALUE
const val BYTE_BITS = Byte.SIZE_BITS
const val BYTE_BYTES = Byte.SIZE_BYTES

val UBYTE_MAX = UByte.MAX_VALUE
val UBYTE_MIN = UByte.MIN_VALUE
const val UBYTE_BITS = UByte.SIZE_BITS
const val UBYTE_BYTES = UByte.SIZE_BYTES

const val CHAR_MAX = Char.MAX_VALUE
const val CHAR_MIN = Char.MIN_VALUE
const val CHAR_BITS = Char.SIZE_BITS
const val CHAR_BYTES = Char.SIZE_BYTES

const val DOUBLE_MAX = Double.MAX_VALUE
const val DOUBLE_MIN = Double.MIN_VALUE
const val DOUBLE_BITS = Double.SIZE_BITS
const val DOUBLE_BYTES = Double.SIZE_BYTES

const val FLOAT_MAX = Float.MAX_VALUE
const val FLOAT_MIN = Float.MIN_VALUE
const val FLOAT_BITS = Float.SIZE_BITS
const val FLOAT_BYTES = Float.SIZE_BYTES
