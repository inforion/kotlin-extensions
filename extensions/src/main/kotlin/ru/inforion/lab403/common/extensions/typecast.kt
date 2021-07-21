@file:Suppress("unused")

package ru.inforion.lab403.common.extensions

import java.math.BigInteger
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
inline val Long.big_integer get() = toBigInteger()

inline val Long.float get() = toFloat()
inline val Long.double get() = toDouble()
inline val Long.char get() = Char(ushort)

@Deprecated("Use truth/untruth instead", ReplaceWith("this.truth"))
inline val Long.bool get() = this != 0L

inline val Long.untruth get() = this == 0L
inline val Long.truth get() = !untruth

/**
 * Int conversions
 */

inline val Int.long_s get() = toLong()
@Deprecated("Conversion from Int to Int is useless", ReplaceWith("this"))
inline val Int.int get() = toInt()
inline val Int.short get() = toShort()
inline val Int.byte get() = toByte()
inline val Int.big_integer get() = toBigInteger()

inline val Int.ulong_s get() = toULong()
inline val Int.uint get() = toUInt()
inline val Int.ushort get() = toUShort()
inline val Int.ubyte get() = toUByte()

inline val Int.long_z get() = long_s and 0xFFFF_FFFF
inline val Int.ulong_z get() = ulong_s and 0xFFFF_FFFFu

inline val Int.float get() = toFloat()
inline val Int.double get() = toDouble()
inline val Int.char get() = toChar()

@Deprecated("Use truth/untruth instead", ReplaceWith("this.truth"))
inline val Int.bool get() = this != 0

inline val Int.untruth get() = this == 0
inline val Int.truth get() = !untruth

/**
 * BigInteger conversions
 */

inline val BigInteger.long_s get() = toLong()
inline val BigInteger.int get() = toInt()
inline val BigInteger.short get() = toShort()
inline val BigInteger.byte get() = toByte()

inline val BigInteger.ulong_s get() = long_s.toULong()
inline val BigInteger.uint get() = int.toUInt()
inline val BigInteger.ushort get() = short.toUShort()
inline val BigInteger.ubyte get() = byte.toUByte()

inline val BigInteger.long_z get() = long_s and 0xFFFF_FFFF
inline val BigInteger.ulong_z get() = ulong_s and 0xFFFF_FFFFu

inline val BigInteger.float get() = toFloat()
inline val BigInteger.double get() = toDouble()
inline val BigInteger.char get() = toChar()

inline val BigInteger.bool get() = int != 0

inline val BigInteger.untruth get() = int == 0
inline val BigInteger.truth get() = !untruth

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

@Deprecated("Use truth/untruth instead", ReplaceWith("this.truth"))
inline val Short.bool get() = int_s != 0

inline val Short.untruth get() = int_s == 0
inline val Short.truth get() = !untruth

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

@Deprecated("Use truth/untruth instead", ReplaceWith("this.truth"))
inline val Byte.bool get() = int_s != 0

inline val Byte.untruth get() = int_s == 0
inline val Byte.truth get() = !untruth

/**
 * Char to signed conversions
 */

inline val Char.long_s get() = code.long_s
inline val Char.int_s get() = code
inline val Char.short get() = code.short
inline val Char.byte get() = code.byte

inline val Char.long_z8 get() = long_s and 0xFFL
inline val Char.int_z8 get() = int_s and 0xFF

inline val Char.long_z16 get() = long_s and 0xFFFFL
inline val Char.int_z16 get() = int_s and 0xFFFF

inline val Char.ulong_s get() = code.ulong_s
inline val Char.uint_s get() = code.uint
inline val Char.ushort get() = code.ushort
inline val Char.ubyte get() = code.ubyte

inline val Char.ulong_z8 get() = ulong_s and 0xFFu
inline val Char.uint_z8 get() = uint_s and 0xFFu

inline val Char.ulong_z16 get() = ulong_s and 0xFFFFu
inline val Char.uint_z16 get() = uint_s and 0xFFFFu

inline val Char.float get() = code.toFloat()
inline val Char.double get() = code.toDouble()
@Deprecated("Conversion from Char to Char is useless", ReplaceWith("this"))
inline val Char.char get() = toChar()

@Deprecated("Use truth/untruth instead", ReplaceWith("this.truth"))
inline val Char.bool get() = code != 0

inline val Char.untruth get() = code == 0
inline val Char.truth get() = !untruth

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

@Deprecated("Use truth/untruth instead", ReplaceWith("this.truth"))
inline val ULong.bool get() = this != 0uL

inline val ULong.untruth get() = this == 0uL
inline val ULong.truth get() = !untruth

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

@Deprecated("Use truth/untruth instead", ReplaceWith("this.truth"))
inline val UInt.bool get() = this != 0u

inline val UInt.untruth get() = this == 0u
inline val UInt.truth get() = !untruth

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

inline val UShort.long_s get() = short.long_s
inline val UShort.int_s get() = short.int_s

inline val UShort.ulong_s get() = short.ulong_s
inline val UShort.uint_s get() = short.uint_s

@Deprecated("Use truth/untruth instead", ReplaceWith("this.truth"))
inline val UShort.bool get() = int_z != 0

inline val UShort.untruth get() = int_z == 0
inline val UShort.truth get() = !untruth

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

inline val UByte.long_s get() = byte.long_s
inline val UByte.int_s get() = byte.int_s
inline val UByte.short_s get() = byte.short_s

inline val UByte.ulong_s get() = byte.ulong_s
inline val UByte.uint_s get() = byte.uint_s
inline val UByte.ushort_s get() = byte.ushort_s

@Deprecated("Use truth/untruth instead", ReplaceWith("this.truth"))
inline val UByte.bool get() = int_z != 0

inline val UByte.untruth get() = int_z == 0
inline val UByte.truth get() = !untruth

/**
 * Float and double conversions
 */

inline val Float.ulong get() = toULong()
inline val Float.uint get() = toUInt()
inline val Float.int get() = toInt()
inline val Float.long get() = toLong()
inline val Float.short get() = toInt().toShort()
inline val Float.byte get() = toInt().toByte()
@Deprecated("Conversion from Float to Float is useless", ReplaceWith("this"))
inline val Float.float get() = toFloat()
inline val Float.double get() = toDouble()

inline val Double.ulong get() = toULong()
inline val Double.uint get() = toUInt()
inline val Double.long get() = toLong()
inline val Double.int get() = toInt()
inline val Double.short get() = toInt().toShort()
inline val Double.byte get() = toInt().toByte()
inline val Double.float get() = toFloat()
@Deprecated("Conversion from Double to Double is useless", ReplaceWith("this"))
inline val Double.double get() = toDouble()

/**
 * Numbers
 */

inline val Number.double get() = toDouble()
inline val Number.float get() = toFloat()

inline val Number.char get() = toChar()

inline val Number.long get() = toLong()
inline val Number.int get() = toInt()
inline val Number.short get() = toShort()
inline val Number.byte get() = toByte()

inline val Number.ulong get() = long.ulong
inline val Number.uint get() = int.uint
inline val Number.ushort get() = short.ushort
inline val Number.ubyte get() = byte.ubyte
