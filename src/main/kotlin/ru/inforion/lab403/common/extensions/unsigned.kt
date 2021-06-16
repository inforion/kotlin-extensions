@file:Suppress("NOTHING_TO_INLINE", "EXPERIMENTAL_API_USAGE", "unused")

package ru.inforion.lab403.common.extensions

import kotlin.toULong
import kotlin.toUInt
import kotlin.toUShort
import kotlin.toUByte

/**
 * Signed value to unsigned conversion
 */
inline val Long.ulong get() = toULong()
inline val Int.ulong get() = toULong()
inline val Short.ulong get() = toULong()
inline val Byte.ulong get() = toULong()

inline val Long.uint get() = toUInt()
inline val Int.uint get() = toUInt()
inline val Short.uint get() = toUInt()
inline val Byte.uint get() = toUInt()

inline val Long.ushort get() = toUShort()
inline val Int.ushort get() = toUShort()
inline val Short.ushort get() = toUShort()
inline val Byte.ushort get() = toUShort()

inline val Long.ubyte get() = toUByte()
inline val Int.ubyte get() = toUByte()
inline val Short.ubyte get() = toUByte()
inline val Byte.ubyte get() = toUByte()

/**
 * Unsigned value to signed conversion
 */
inline val ULong.long get() = toLong()
inline val ULong.int get() = toInt()
inline val ULong.short get() = toShort()
inline val ULong.byte get() = toByte()

inline val UInt.long get() = toLong()
inline val UInt.int get() = toInt()
inline val UInt.short get() = toShort()
inline val UInt.byte get() = toByte()

inline val UShort.long get() = toLong()
inline val UShort.int get() = toInt()
inline val UShort.short get() = toShort()
inline val UShort.byte get() = toByte()

inline val UByte.long get() = toLong()
inline val UByte.int get() = toInt()
inline val UByte.short get() = toShort()
inline val UByte.byte get() = toByte()

/**
 * Unsigned value to unsigned conversion
 */
inline val ULong.ulong get() = toULong()
inline val ULong.uint get() = toUInt()
inline val ULong.ushort get() = toUShort()
inline val ULong.ubyte get() = toUByte()

inline val UInt.ulong get() = toULong()
inline val UInt.uint get() = toUInt()
inline val UInt.ushort get() = toUShort()
inline val UInt.ubyte get() = toUByte()

inline val UShort.ulong get() = toULong()
inline val UShort.uint get() = toUInt()
inline val UShort.ushort get() = toUShort()
inline val UShort.ubyte get() = toUByte()

inline val UByte.ulong get() = toULong()
inline val UByte.uint get() = toUInt()
inline val UByte.ushort get() = toUShort()
inline val UByte.ubyte get() = toUByte()

inline fun ubitMask(size: Int): ULong {
    require(size in 1..64) { "Size must be in range 1..64" }
    return ULong.MAX_VALUE shr (64 - size)
}

inline fun ubitMask(range: IntRange): ULong = if (range.last == 0) ubitMask(range.first + 1) else
    ubitMask(range.first + 1) and ubitMask(range.last).inv()

/**
 * Calculate inverse value
 */
inline fun inv(data: ULong) = data.inv()
inline fun inv(data: UInt) = data.inv()
inline fun inv(data: UShort) = data.inv()
inline fun inv(data: UByte) = data.inv()

/**
 * Fill with zeros bit outside the specified range (from msb to 0)
 */
inline infix fun ULong.mask(size: Int) = this and ubitMask(size).ulong
inline infix fun UInt.mask(size: Int) = this and ubitMask(size).uint
inline infix fun UShort.mask(size: Int) = this and ubitMask(size).ushort
inline infix fun UByte.mask(size: Int) = this and ubitMask(size).ubyte

/**
 * Fill with zeros bit outside the specified range (from msb to lsb)
 */
inline infix fun ULong.mask(range: IntRange) = this and bitMask(range).ulong
inline infix fun UInt.mask(range: IntRange) = this and bitMask(range).uint
inline infix fun UShort.mask(range: IntRange) = this and bitMask(range).ushort
inline infix fun UByte.mask(range: IntRange) = this and bitMask(range).ubyte

/**
 * Fill with zero specified bit range (from msb to lsb)
 */
inline infix fun ULong.bzero(range: IntRange) = this and inv(ubitMask(range)).ulong
inline infix fun UInt.bzero(range: IntRange) = this and inv(ubitMask(range)).uint
inline infix fun UShort.bzero(range: IntRange) = this and inv(ubitMask(range)).ushort
inline infix fun UByte.bzero(range: IntRange) = this and inv(ubitMask(range)).ubyte

inline fun insertField(dst: ULong, src: ULong, range: IntRange) = (dst bzero range) or ((src shl range.last) mask range)
inline fun insertField(dst: UInt, src: UInt, range: IntRange) = (dst bzero range) or ((src shl range.last) mask range)

inline fun insertBit(dst: ULong, value: ULong, indx: Int): ULong {
    val ins = value shl indx
    val mask = (1UL shl indx).inv()
    return dst and mask or ins
}

inline fun insertBit(dst: UInt, value: UInt, indx: Int): UInt {
    val ins = value shl indx
    val mask = (1U shl indx).inv()
    return dst and mask or ins
}

inline fun ULong.insert(value: ULong, index: Int) = insertBit(this, value, index)
inline fun UInt.insert(value: UInt, index: Int) = insertBit(this, value, index)

inline fun ULong.insert(data: ULong, range: IntRange) = insertField(this, data, range)
inline fun UInt.insert(data: UInt, range: IntRange) = insertField(this, data, range)

inline fun insert(value: ULong, index: Int) = 0UL.insert(value, index)
inline fun insert(value: UInt, index: Int) = 0U.insert(value, index)

inline fun insert(data: ULong, range: IntRange) = 0UL.insert(data, range)
inline fun insert(data: UInt, range: IntRange) = 0U.insert(data, range)

inline val ULong.hex get() = toString(16)
inline val ULong.hex2 get() = "%02X".format(long)
inline val ULong.hex4 get() = "%04X".format(long)
inline val ULong.hex8 get() = "%08X".format(long)
inline val ULong.hex16 get() = "%016X".format(long)

inline val UInt.hex get() = toString(16)
inline val UInt.hex2 get() = "%02X".format(int)
inline val UInt.hex4 get() = "%04X".format(int)
inline val UInt.hex8 get() = "%08X".format(int)

inline val UShort.hex get() = toString(16)
inline val UShort.hex2 get() = "%02X".format(short)
inline val UShort.hex4 get() = "%04X".format(short)

inline val UByte.hex get() = toString(16)
inline val UByte.hex2 get() = "%02X".format(byte)