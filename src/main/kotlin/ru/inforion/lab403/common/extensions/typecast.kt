@file:Suppress("unused")

package ru.inforion.lab403.common.extensions

import kotlin.experimental.and

/**
 * Long conversions
 */

@Deprecated("Conversion from Long to Long is useless", ReplaceWith("this"))
inline val Long.long get() = toLong()
inline val Long.int get() = toInt()
inline val Long.short get() = toShort()
inline val Long.byte get() = toByte()

inline val Long.ulong get() = toULong()
inline val Long.uint get() = toUInt()
inline val Long.ushort get() = toUShort()
inline val Long.ubyte get() = toUByte()

inline val Long.float get() = toFloat()
inline val Long.double get() = toDouble()
inline val Long.char get() = Char(ushort)
inline val Long.bool get() = this != 0L

/**
 * Int conversions
 */

inline val Int.long_s get() = toLong()
@Deprecated("Conversion from Int to Int is useless", ReplaceWith("this"))
inline val Int.int get() = toInt()
inline val Int.short get() = toShort()
inline val Int.byte get() = toByte()

inline val Int.ulong_s get() = toULong()
inline val Int.uint get() = toUInt()
inline val Int.ushort get() = toUShort()
inline val Int.ubyte get() = toUByte()

inline val Int.long_z get() = long_s and 0xFFFF_FFFF
inline val Int.ulong_z get() = ulong_s and 0xFFFF_FFFFu

inline val Int.float get() = toFloat()
inline val Int.double get() = toDouble()
inline val Int.char get() = toChar()
inline val Int.bool get() = this != 0

/**
 * Short conversions
 */

inline val Short.long_s get() = toLong()
inline val Short.int_s get() = toInt()
@Deprecated("Conversion from Short to Short is useless", ReplaceWith("this"))
inline val Short.short get() = toShort()
inline val Short.byte get() = toByte()

inline val Short.ulong_s get() = toULong()
inline val Short.uint_s get() = toUInt()
inline val Short.ushort get() = toUShort()
inline val Short.ubyte get() = toUByte()

inline val Short.long_z get() = long_s and 0xFFFF
inline val Short.int_z get() = int_s and 0xFFFF

inline val Short.ulong_z get() = ulong_s and 0xFFFFu
inline val Short.uint_z get() = uint_s and 0xFFFFu

inline val Short.float get() = toFloat()
inline val Short.double get() = toDouble()
inline val Short.char get() = Char(ushort)
inline val Short.bool get() = int_s != 0

/**
 * Byte conversions
 */

inline val Byte.long_s get() = toLong()
inline val Byte.int_s get() = toInt()
inline val Byte.short_s get() = toShort()
@Deprecated("Conversion from Byte to Byte is useless", ReplaceWith("this"))
inline val Byte.byte get() = toByte()

inline val Byte.ulong_s get() = toULong()
inline val Byte.uint_s get() = toUInt()
inline val Byte.ushort_s get() = toUShort()
inline val Byte.ubyte get() = toUByte()

inline val Byte.long_z get() = long_s and 0xFF
inline val Byte.int_z get() = int_s and 0xFF
inline val Byte.short_z get() = short_s and 0xFF

inline val Byte.ulong_z get() = ulong_s and 0xFFu
inline val Byte.uint_z get() = uint_s and 0xFFu
inline val Byte.ushort_z get() = ushort_s and 0xFFu

inline val Byte.float get() = toFloat()
inline val Byte.double get() = toDouble()
inline val Byte.char get() = Char(ushort_s)
inline val Byte.bool get() = int_s != 0

/**
 * Char to signed conversions
 */

inline val Char.long_s get() = code.toLong()
inline val Char.int_s get() = code
inline val Char.short_s get() = code.toShort()
inline val Char.byte_s get() = code.toByte()

inline val Char.ulong_s get() = code.toULong()
inline val Char.uint_s get() = code.toUInt()
inline val Char.ushort_s get() = code.toUShort()
inline val Char.ubyte_s get() = code.toUByte()

inline val Char.float get() = code.toFloat()
inline val Char.double get() = code.toDouble()
@Deprecated("Conversion from Char to Char is useless", ReplaceWith("this"))
inline val Char.char get() = toChar()

inline val Char.bool get() = code != 0

/**
 * ULong conversions
 */

inline val ULong.long get() = toLong()
inline val ULong.int get() = toInt()
inline val ULong.short get() = toShort()
inline val ULong.byte get() = toByte()

@Deprecated("Conversion from ULong to ULong is useless", ReplaceWith("this"))
inline val ULong.ulong get() = toULong()
inline val ULong.uint get() = toUInt()
inline val ULong.ushort get() = toUShort()
inline val ULong.ubyte get() = toUByte()

inline val ULong.float get() = toFloat()
inline val ULong.double get() = toDouble()
inline val ULong.char get() = Char(ushort)

inline val ULong.bool get() = this != 0uL

/**
 * UInt conversions
 */

inline val UInt.long_z get() = toLong()
inline val UInt.int get() = toInt()
inline val UInt.short get() = toShort()
inline val UInt.byte get() = toByte()

inline val UInt.ulong_z get() = toULong()
@Deprecated("Conversion from UInt to UInt is useless", ReplaceWith("this"))
inline val UInt.uint get() = toUInt()
inline val UInt.ushort get() = toUShort()
inline val UInt.ubyte get() = toUByte()

inline val UInt.bool get() = this != 0u

/**
 * UShort conversions
 */

inline val UShort.long_z get() = toLong()
inline val UShort.int_z get() = toInt()
inline val UShort.short get() = toShort()
inline val UShort.byte get() = toByte()

inline val UShort.ulong_z get() = toULong()
inline val UShort.uint_z get() = toUInt()
@Deprecated("Conversion from UShort to UShort is useless", ReplaceWith("this"))
inline val UShort.ushort get() = toUShort()
inline val UShort.ubyte get() = toUByte()

inline val UShort.bool get() = int_z != 0

/**
 * UByte conversions
 */

inline val UByte.long_z get() = toLong()
inline val UByte.int_z get() = toInt()
inline val UByte.short_z get() = toShort()
inline val UByte.byte get() = toByte()

inline val UByte.ulong_z get() = toULong()
inline val UByte.uint_z get() = toUInt()
inline val UByte.ushort_z get() = toUShort()
@Deprecated("Conversion from UByte to UByte is useless", ReplaceWith("this"))
inline val UByte.ubyte get() = toUByte()

inline val UByte.bool get() = int_z != 0

/**
 * Float and double conversions
 */

inline val Float.long get() = toLong()
inline val Float.int get() = toInt()
inline val Float.short get() = toInt().toShort()
inline val Float.byte get() = toInt().toByte()
@Deprecated("Conversion from Float to Float is useless", ReplaceWith("this"))
inline val Float.float get() = toFloat()
inline val Float.double get() = toDouble()

inline val Double.long get() = toLong()
inline val Double.int get() = toInt()
inline val Double.short get() = toInt().toShort()
inline val Double.byte get() = toInt().toByte()
inline val Double.float get() = toFloat()
@Deprecated("Conversion from Double to Double is useless", ReplaceWith("this"))
inline val Double.double get() = toDouble()