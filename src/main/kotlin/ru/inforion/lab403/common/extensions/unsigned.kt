@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions

import kotlin.toULong
import kotlin.toUInt
import kotlin.toUShort
import kotlin.toUByte

/**
 * Signed value to unsigned conversion
 */
@ExperimentalUnsignedTypes val Long.ulong get() = toULong()
@ExperimentalUnsignedTypes val Int.ulong get() = toULong()
@ExperimentalUnsignedTypes val Short.ulong get() = toULong()
@ExperimentalUnsignedTypes val Byte.ulong get() = toULong()

@ExperimentalUnsignedTypes val Long.uint get() = toUInt()
@ExperimentalUnsignedTypes val Int.uint get() = toUInt()
@ExperimentalUnsignedTypes val Short.uint get() = toUInt()
@ExperimentalUnsignedTypes val Byte.uint get() = toUInt()

@ExperimentalUnsignedTypes val Long.ushort get() = toUShort()
@ExperimentalUnsignedTypes val Int.ushort get() = toUShort()
@ExperimentalUnsignedTypes val Short.ushort get() = toUShort()
@ExperimentalUnsignedTypes val Byte.ushort get() = toUShort()

@ExperimentalUnsignedTypes val Long.ubyte get() = toUByte()
@ExperimentalUnsignedTypes val Int.ubyte get() = toUByte()
@ExperimentalUnsignedTypes val Short.ubyte get() = toUByte()
@ExperimentalUnsignedTypes val Byte.ubyte get() = toUByte()

/**
 * Unsigned value to signed conversion
 */
@ExperimentalUnsignedTypes val ULong.long get() = toLong()
@ExperimentalUnsignedTypes val ULong.int get() = toInt()
@ExperimentalUnsignedTypes val ULong.short get() = toShort()
@ExperimentalUnsignedTypes val ULong.byte get() = toByte()

@ExperimentalUnsignedTypes val UInt.long get() = toLong()
@ExperimentalUnsignedTypes val UInt.int get() = toInt()
@ExperimentalUnsignedTypes val UInt.short get() = toShort()
@ExperimentalUnsignedTypes val UInt.byte get() = toByte()

@ExperimentalUnsignedTypes val UShort.long get() = toLong()
@ExperimentalUnsignedTypes val UShort.int get() = toInt()
@ExperimentalUnsignedTypes val UShort.short get() = toShort()
@ExperimentalUnsignedTypes val UShort.byte get() = toByte()

@ExperimentalUnsignedTypes val UByte.long get() = toLong()
@ExperimentalUnsignedTypes val UByte.int get() = toInt()
@ExperimentalUnsignedTypes val UByte.short get() = toShort()
@ExperimentalUnsignedTypes val UByte.byte get() = toByte()

/**
 * Unsigned value to unsigned conversion
 */
@ExperimentalUnsignedTypes val ULong.ulong get() = toULong()
@ExperimentalUnsignedTypes val ULong.uint get() = toUInt()
@ExperimentalUnsignedTypes val ULong.ushort get() = toUShort()
@ExperimentalUnsignedTypes val ULong.ubyte get() = toUByte()

@ExperimentalUnsignedTypes val UInt.ulong get() = toULong()
@ExperimentalUnsignedTypes val UInt.uint get() = toUInt()
@ExperimentalUnsignedTypes val UInt.ushort get() = toUShort()
@ExperimentalUnsignedTypes val UInt.ubyte get() = toUByte()

@ExperimentalUnsignedTypes val UShort.ulong get() = toULong()
@ExperimentalUnsignedTypes val UShort.uint get() = toUInt()
@ExperimentalUnsignedTypes val UShort.ushort get() = toUShort()
@ExperimentalUnsignedTypes val UShort.ubyte get() = toUByte()

@ExperimentalUnsignedTypes val UByte.ulong get() = toULong()
@ExperimentalUnsignedTypes val UByte.uint get() = toUInt()
@ExperimentalUnsignedTypes val UByte.ushort get() = toUShort()
@ExperimentalUnsignedTypes val UByte.ubyte get() = toUByte()

@ExperimentalUnsignedTypes
inline fun ubitMask(size: Int): ULong {
    require(size in 1..64) { "Size must be in range 1..64" }
    return ULong.MAX_VALUE shr (64 - size)
}

@ExperimentalUnsignedTypes
inline fun ubitMask(range: IntRange): ULong = if (range.last == 0) ubitMask(range.first + 1) else
    ubitMask(range.first + 1) and ubitMask(range.last).inv()

/**
 * Calculate inverse value
 */
@ExperimentalUnsignedTypes inline fun inv(data: ULong) = data.inv()
@ExperimentalUnsignedTypes inline fun inv(data: UInt) = data.inv()
@ExperimentalUnsignedTypes inline fun inv(data: UShort) = data.inv()
@ExperimentalUnsignedTypes inline fun inv(data: UByte) = data.inv()

/**
 * Fill with zeros bit outside the specified range (from msb to 0)
 */
@ExperimentalUnsignedTypes inline infix fun ULong.mask(size: Int) = this and ubitMask(size).ulong
@ExperimentalUnsignedTypes inline infix fun UInt.mask(size: Int) = this and ubitMask(size).uint
@ExperimentalUnsignedTypes inline infix fun UShort.mask(size: Int) = this and ubitMask(size).ushort
@ExperimentalUnsignedTypes inline infix fun UByte.mask(size: Int) = this and ubitMask(size).ubyte

/**
 * Fill with zeros bit outside the specified range (from msb to lsb)
 */
@ExperimentalUnsignedTypes inline infix fun ULong.mask(range: IntRange) = this and bitMask(range).ulong
@ExperimentalUnsignedTypes inline infix fun UInt.mask(range: IntRange) = this and bitMask(range).uint
@ExperimentalUnsignedTypes inline infix fun UShort.mask(range: IntRange) = this and bitMask(range).ushort
@ExperimentalUnsignedTypes inline infix fun UByte.mask(range: IntRange) = this and bitMask(range).ubyte

/**
 * Fill with zero specified bit range (from msb to lsb)
 */
@ExperimentalUnsignedTypes inline infix fun ULong.bzero(range: IntRange) = this and inv(ubitMask(range)).ulong
@ExperimentalUnsignedTypes inline infix fun UInt.bzero(range: IntRange) = this and inv(ubitMask(range)).uint
@ExperimentalUnsignedTypes inline infix fun UShort.bzero(range: IntRange) = this and inv(ubitMask(range)).ushort
@ExperimentalUnsignedTypes inline infix fun UByte.bzero(range: IntRange) = this and inv(ubitMask(range)).ubyte

@ExperimentalUnsignedTypes inline fun insertField(dst: ULong, src: ULong, range: IntRange) = (dst bzero range) or ((src shl range.last) mask range)
@ExperimentalUnsignedTypes inline fun insertField(dst: UInt, src: UInt, range: IntRange) = (dst bzero range) or ((src shl range.last) mask range)

@ExperimentalUnsignedTypes
inline fun insertBit(dst: ULong, value: ULong, indx: Int): ULong {
    val ins = value shl indx
    val mask = (1UL shl indx).inv()
    return dst and mask or ins
}

@ExperimentalUnsignedTypes
inline fun insertBit(dst: UInt, value: UInt, indx: Int): UInt {
    val ins = value shl indx
    val mask = (1U shl indx).inv()
    return dst and mask or ins
}

@ExperimentalUnsignedTypes inline fun ULong.insert(value: ULong, indx: Int) = insertBit(this, value, indx)
@ExperimentalUnsignedTypes inline fun UInt.insert(value: UInt, indx: Int) = insertBit(this, value, indx)

@ExperimentalUnsignedTypes inline fun ULong.insert(data: ULong, range: IntRange) = insertField(this, data, range)
@ExperimentalUnsignedTypes inline fun UInt.insert(data: UInt, range: IntRange) = insertField(this, data, range)

@ExperimentalUnsignedTypes inline fun insert(value: ULong, indx: Int) = 0UL.insert(value, indx)
@ExperimentalUnsignedTypes inline fun insert(value: UInt, indx: Int) = 0U.insert(value, indx)

@ExperimentalUnsignedTypes inline fun insert(data: ULong, range: IntRange) = 0UL.insert(data, range)
@ExperimentalUnsignedTypes inline fun insert(data: UInt, range: IntRange) = 0U.insert(data, range)

@ExperimentalUnsignedTypes val ULong.hex get() = toString(16)
@ExperimentalUnsignedTypes val ULong.hex2 get() = "%02X".format(long)
@ExperimentalUnsignedTypes val ULong.hex4 get() = "%04X".format(long)
@ExperimentalUnsignedTypes val ULong.hex8 get() = "%08X".format(long)
@ExperimentalUnsignedTypes val ULong.hex16 get() = "%016X".format(long)

@ExperimentalUnsignedTypes val UInt.hex get() = toString(16)
@ExperimentalUnsignedTypes val UInt.hex2 get() = "%02X".format(int)
@ExperimentalUnsignedTypes val UInt.hex4 get() = "%04X".format(int)
@ExperimentalUnsignedTypes val UInt.hex8 get() = "%08X".format(int)

@ExperimentalUnsignedTypes val UShort.hex get() = toString(16)
@ExperimentalUnsignedTypes val UShort.hex2 get() = "%02X".format(short)
@ExperimentalUnsignedTypes val UShort.hex4 get() = "%04X".format(short)

@ExperimentalUnsignedTypes val UByte.hex get() = toString(16)
@ExperimentalUnsignedTypes val UByte.hex2 get() = "%02X".format(byte)