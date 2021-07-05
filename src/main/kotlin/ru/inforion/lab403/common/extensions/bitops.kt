@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.extensions

import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.xor

// =====================================================================================================================
// Shift operations
// =====================================================================================================================

inline infix fun Long.ashr(n: Int) = this shr n

inline infix fun Int.ashr(n: Int) = this shr n

inline infix fun Short.shl(n: Int) = (int_s shl n).short
inline infix fun Short.ashr(n: Int) = (int_s shr n).short
inline infix fun Short.ushr(n: Int) = (int_z shr n).short

inline infix fun Byte.shl(count: Int) = (int_s shl count).byte
inline infix fun Byte.ashr(count: Int) = (int_s shr count).byte
inline infix fun Byte.ushr(count: Int) = (int_z ushr count).byte

inline infix fun ULong.ashr(n: Int) = (long shr n).ulong
inline infix fun ULong.ushr(n: Int) = this shr n

inline infix fun UInt.ashr(n: Int) = (int shr n).uint
inline infix fun UInt.ushr(n: Int) = this shr n

inline infix fun UShort.shl(n: Int) = (uint_z shl n).ushort
inline infix fun UShort.ashr(n: Int) = (int_z ashr n).ushort
inline infix fun UShort.ushr(n: Int) = (uint_z shr n).ushort

inline infix fun UByte.shl(n: Int) = (uint_z shr n).ubyte
inline infix fun UByte.ashr(n: Int) = (int_z shr n).ubyte
inline infix fun UByte.ushr(n: Int) = (uint_z shr n).ubyte

// =====================================================================================================================
// Bit inverse operations
// =====================================================================================================================

inline fun inv(data: Long) = data.inv()
inline fun inv(data: Int) = data.inv()
inline fun inv(data: Short) = data.inv()
inline fun inv(data: Byte) = data.inv()

inline fun inv(data: ULong) = data.inv()
inline fun inv(data: UInt) = data.inv()
inline fun inv(data: UShort) = data.inv()
inline fun inv(data: UByte) = data.inv()

inline fun Int.bitReverse(): Int {
    var x = ulong_z
    x = (((x and 0xaaaaaaaauL) ushr 1) or ((x and 0x55555555uL) shl 1))
    x = (((x and 0xccccccccuL) ushr 2) or ((x and 0x33333333uL) shl 2))
    x = (((x and 0xf0f0f0f0uL) ushr 4) or ((x and 0x0f0f0f0fuL) shl 4))
    x = (((x and 0xff00ff00uL) ushr 8) or ((x and 0x00ff00ffuL) shl 8))
    return ((x ushr 16) or (x shl 16)).int
}

inline operator fun ULong.not() = (this and 1u) xor 1u
inline operator fun UInt.not() = (this and 1u) xor 1u
inline operator fun UShort.not() = (this and 1u) xor 1u
inline operator fun UByte.not() = (this and 1u) xor 1u

inline operator fun Long.not() = (this and 1) xor 1
inline operator fun Int.not() = (this and 1) xor 1
inline operator fun Short.not() = (this and 1) xor 1
inline operator fun Byte.not() = (this and 1) xor 1

// =====================================================================================================================
// Create bit mask operations
// =====================================================================================================================

inline fun ubitMask32(size: Int) = UInt.MAX_VALUE ushr (UInt.SIZE_BITS - size)
inline fun ubitMask64(size: Int) = ULong.MAX_VALUE ushr (ULong.SIZE_BITS - size)

inline fun ubitMask32(range: IntRange) = if (range.last == 0) ubitMask32(range.first + 1) else
    ubitMask32(range.first + 1) and inv(ubitMask32(range.last))

inline fun ubitMask64(range: IntRange) = if (range.last == 0) ubitMask64(range.first + 1) else
    ubitMask64(range.first + 1) and inv(ubitMask64(range.last))

inline fun bitMask32(size: Int) = ubitMask32(size).int
inline fun bitMask64(size: Int) = ubitMask64(size).long

inline fun bitMask32(range: IntRange) = ubitMask32(range).int
inline fun bitMask64(range: IntRange) = ubitMask64(range).long

// =====================================================================================================================
// Fill with zeros bit outside the specified range for long values
// =====================================================================================================================

inline infix fun ULong.mask(size: Int) = this and ubitMask64(size)
inline infix fun UInt.mask(size: Int) = this and ubitMask32(size)
inline infix fun UShort.mask(size: Int) = (uint_z mask size).ushort
inline infix fun UByte.mask(size: Int) = (uint_z mask size).ubyte

inline infix fun Long.mask(size: Int) = (ulong mask size).long
inline infix fun Int.mask(size: Int) = (uint mask size).int
inline infix fun Short.mask(size: Int) = (ushort mask size).short
inline infix fun Byte.mask(size: Int) = (ubyte mask size).byte

inline infix fun ULong.mask(range: IntRange) = this and ubitMask64(range)
inline infix fun UInt.mask(range: IntRange) = this and ubitMask32(range)
inline infix fun UShort.mask(range: IntRange) = (uint_z mask range).ushort
inline infix fun UByte.mask(range: IntRange) = (uint_z mask range).ubyte

inline infix fun Long.mask(range: IntRange) = (ulong mask range).long
inline infix fun Int.mask(range: IntRange) = (uint mask range).int
inline infix fun Short.mask(range: IntRange) = (ushort mask range).short
inline infix fun Byte.mask(range: IntRange) = (ubyte mask range).byte

// =====================================================================================================================
// Fill with zero specified bit range (from msb to lsb)
// =====================================================================================================================

inline infix fun ULong.bzero(range: IntRange) = this and inv(ubitMask64(range))
inline infix fun UInt.bzero(range: IntRange) = this and inv(ubitMask32(range))
inline infix fun UShort.bzero(range: IntRange) = (uint_z bzero range).ushort
inline infix fun UByte.bzero(range: IntRange) = (uint_z bzero range).ubyte

inline infix fun Long.bzero(range: IntRange) = (ulong bzero range).long
inline infix fun Int.bzero(range: IntRange) = (uint bzero range).int
inline infix fun Short.bzero(range: IntRange) = (ushort bzero range).short
inline infix fun Byte.bzero(range: IntRange) = (ubyte bzero range).byte

/**
 * Make bit extension by the lowest bit of the id.
 * for 0x034251.bext(3) return 0b111
 * for 0x031400.bext(10) return 0
 */
inline fun Number.bext(n: Int): ULong {
    val bit = toInt() and 1
    return if (bit == 1) ubitMask64(n) else 0uL
}

// =====================================================================================================================
// Bit extract operations
//
// Note: results commonly are Int or UInt for more convinient operations after bit extraction
// =====================================================================================================================

inline fun ULong.xbits(high: Int, low: Int) = (this ushr low) and ((1uL shl (high - low + 1)) - 1u)
inline fun UInt.xbits(high: Int, low: Int) = (this ushr low) and ((1u shl (high - low + 1)) - 1u)
inline fun UShort.xbits(high: Int, low: Int) = uint_z.xbits(high, low)
inline fun UByte.xbits(high: Int, low: Int) = uint_z.xbits(high, low)

inline fun Long.xbits(high: Int, low: Int) = ulong.xbits(high, low).long
inline fun Int.xbits(high: Int, low: Int) = uint.xbits(high, low).int
inline fun Short.xbits(high: Int, low: Int) = ushort.xbits(high, low).int
inline fun Byte.xbits(high: Int, low: Int) = ubyte.xbits(high, low).int

inline infix fun ULong.xbit(index: Int) = (this ushr index) and 1u
inline infix fun UInt.xbit(index: Int) = (this ushr index) and 1u
inline infix fun UShort.xbit(index: Int) = uint_z xbit index
inline infix fun UByte.xbit(index: Int) = uint_z xbit index

inline infix fun Long.xbit(index: Int) = (ulong xbit index).long
inline infix fun Int.xbit(index: Int) = (uint xbit index).int
inline infix fun Short.xbit(index: Int) = (ushort xbit index).int
inline infix fun Byte.xbit(index: Int) = (ubyte xbit index).int

inline operator fun ULong.get(range: IntRange) = xbits(range.first, range.last)
inline operator fun UInt.get(range: IntRange) = xbits(range.first, range.last)
inline operator fun UShort.get(range: IntRange) = xbits(range.first, range.last)
inline operator fun UByte.get(range: IntRange) = xbits(range.first, range.last)

inline operator fun Long.get(range: IntRange) = xbits(range.first, range.last)
inline operator fun Int.get(range: IntRange) = xbits(range.first, range.last)
inline operator fun Short.get(range: IntRange) = xbits(range.first, range.last)
inline operator fun Byte.get(range: IntRange) = xbits(range.first, range.last)

inline operator fun ULong.get(index: Int) = xbit(index)
inline operator fun UInt.get(index: Int) = xbit(index)
inline operator fun UShort.get(index: Int) = xbit(index)
inline operator fun UByte.get(index: Int) = xbit(index)

inline operator fun Long.get(index: Int) = xbit(index)
inline operator fun Int.get(index: Int) = xbit(index)
inline operator fun Short.get(index: Int) = xbit(index)
inline operator fun Byte.get(index: Int) = xbit(index)

// =====================================================================================================================
// Bit insert operations
// =====================================================================================================================

inline fun insertBit(dst: ULong, value: ULong, index: Int): ULong {
    val ins = value shl index
    val mask = inv(1uL shl index)
    return dst and mask or ins
}

inline fun insertBit(dst: UInt, value: UInt, index: Int): UInt {
    val ins = value shl index
    val mask = inv(1u shl index)
    return dst and mask or ins
}

inline fun insertBit(dst: Long, value: Long, index: Int): Long = insertBit(dst.ulong, value.ulong, index).long
inline fun insertBit(dst: Int, value: Int, index: Int): Int = insertBit(dst.uint, value.uint, index).int

inline fun insertField(dst: ULong, src: ULong, range: IntRange) = (dst bzero range) or ((src shl range.last) mask range)
inline fun insertField(dst: UInt, src: UInt, range: IntRange) = (dst bzero range) or ((src shl range.last) mask range)

inline fun insertField(dst: Long, src: Long, range: IntRange) = insertField(dst.ulong, src.ulong, range).long
inline fun insertField(dst: Int, src: Int, range: IntRange) = insertField(dst.uint, src.uint, range).int


inline fun ULong.insert(value: ULong, index: Int) = insertBit(this, value, index)
inline fun ULong.insert(value: UInt, index: Int) = insert(value.ulong_z, index)
inline fun ULong.insert(value: Int, index: Int) = insert(value.ulong_z, index)
inline fun ULong.insert(value: Boolean, index: Int) = insert(value.ulong, index)

inline fun UInt.insert(value: UInt, index: Int) = insertBit(this, value, index)
inline fun UInt.insert(value: Int, index: Int) = insert(value.uint, index)
inline fun UInt.insert(value: Boolean, index: Int) = insert(value.ulong, index)

inline fun ULong.insert(data: ULong, range: IntRange) = insertField(this, data, range)
inline fun UInt.insert(data: UInt, range: IntRange) = insertField(this, data, range)

inline fun Long.insert(value: Long, index: Int) = insertBit(ulong, value.ulong, index).long
inline fun Int.insert(value: Int, index: Int) = insertBit(uint, value.uint, index).int

inline fun Long.insert(data: Long, range: IntRange) = insertField(ulong, data.ulong, range).long
inline fun Int.insert(data: Int, range: IntRange) = insertField(uint, data.uint, range).int


inline fun insert(value: ULong, index: Int) = 0uL.insert(value, index)
inline fun insert(value: UInt, index: Int) = 0u.insert(value, index)

inline fun insert(data: ULong, range: IntRange) = 0uL.insert(data, range)
inline fun insert(data: UInt, range: IntRange) = 0u.insert(data, range)

inline fun insert(value: Long, index: Int) = insert(value.ulong, index).long
inline fun insert(value: Int, index: Int) = insert(value.uint, index).int
inline fun insert(value: Boolean, index: Int) = insert(value.uint, index).int

inline fun insert(data: Long, range: IntRange): Long = insert(data.ulong, range).long
inline fun insert(data: Int, range: IntRange): Int = insert(data.uint, range).int

// =====================================================================================================================
// Bit set/clr/toggle operations
// =====================================================================================================================

inline infix fun ULong.clr(range: IntRange) = this bzero range
inline infix fun UInt.clr(range: IntRange) = this bzero range
inline infix fun UShort.clr(range: IntRange) = this bzero range
inline infix fun UByte.clr(range: IntRange) = this bzero range

inline infix fun Long.clr(range: IntRange) = this bzero range
inline infix fun Int.clr(range: IntRange) = this bzero range
inline infix fun Short.clr(range: IntRange) = this bzero range
inline infix fun Byte.clr(range: IntRange) = this bzero range

inline infix fun ULong.clr(index: Int) = this and inv(1uL shl index)
inline infix fun UInt.clr(index: Int) = this and inv(1u shl index)
inline infix fun UShort.clr(index: Int) = (uint_z clr index).ushort
inline infix fun UByte.clr(index: Int) = (uint_z clr index).ubyte

inline infix fun Long.clr(index: Int) = (ulong clr index).long
inline infix fun Int.clr(index: Int) = (uint clr index).int
inline infix fun Short.clr(index: Int) = (int_s clr index).short
inline infix fun Byte.clr(index: Int) = (int_s clr index).byte


inline infix fun ULong.set(index: Int) = this or (1uL shl index)
inline infix fun UInt.set(index: Int) = this or (1u shl index)
inline infix fun UShort.set(index: Int) = (uint_z set index).ushort
inline infix fun UByte.set(index: Int) = (uint_z set index).ubyte

inline infix fun Long.set(index: Int) = (ulong set index).long
inline infix fun Int.set(index: Int) = (uint set index).int
inline infix fun Short.set(index: Int) = (int_s set index).short
inline infix fun Byte.set(index: Int) = (int_s set index).byte


inline infix fun ULong.toggle(index: Int) = this xor (1uL shl index)
inline infix fun UInt.toggle(index: Int) = this xor (1u shl index)
inline infix fun UShort.toggle(index: Int) = (uint_z toggle index).ushort
inline infix fun UByte.toggle(index: Int) = (uint_z toggle index).ubyte

inline infix fun Long.toggle(index: Int) = (ulong toggle index).long
inline infix fun Int.toggle(index: Int) = (uint toggle index).int
inline infix fun Short.toggle(index: Int) = (int_s toggle index).short
inline infix fun Byte.toggle(index: Int) = (int_s toggle index).byte


/**
 * Concatenate two long values 'left' and 'right' at border 'at'
 * i.e. cat(0xFL, 0xAL, 3) = 0xFAL
 */
inline fun cat(left: Long, right: Long, at: Int): Long {
    require(at >= 0)
    return (left shl at + 1).insert(right, at..0)
}

inline fun cat(left: ULong, right: ULong, at: Int): ULong {
    require(at >= 0)
    return (left shl at + 1).insert(right, at..0)
}

/**
 * Concatenate two int values 'left' and 'right' at border 'at'
 * i.e. cat(0xF, 0xA, 3) = 0xFA
 */
inline fun cat(left: Int, right: Int, at: Int): Int {
    require(at >= 0)
    return (left shl at + 1).insert(right, at..0)
}

// =====================================================================================================================
// Byte swap operations
// =====================================================================================================================

inline fun ULong.swap64() =
    (this and 0x00000000_000000FFuL shl  56) or
    (this and 0x00000000_0000FF00uL shl  40) or
    (this and 0x00000000_00FF0000uL shl  24) or
    (this and 0x00000000_FF000000uL shl   8) or
    (this and 0x000000FF_00000000uL ushr  8) or
    (this and 0x0000FF00_00000000uL ushr 24) or
    (this and 0x00FF0000_00000000uL ushr 40) or
    (this and 0xFF000000_00000000uL ushr 56)

inline fun ULong.swap32() =
    (this and 0x0000_00FFuL shl  24) or
    (this and 0x0000_FF00uL shl   8) or
    (this and 0x00FF_0000uL ushr  8) or
    (this and 0xFF00_0000uL ushr 24)

inline fun ULong.swap16() =
    (this and 0x00FFuL shl  8) or
    (this and 0xFF00uL ushr 8)

inline fun UInt.swap32() =
    (this and 0x0000_00FFu shl  24) or
    (this and 0x0000_FF00u shl   8) or
    (this and 0x00FF_0000u ushr  8) or
    (this and 0xFF00_0000u ushr 24)

inline fun UInt.swap16() =
    (this and 0x00FFu shl 8) or
    (this and 0xFF00u ushr 8)

inline fun UShort.swap16() = uint_z.swap16().ushort


inline fun Long.swap64() = ulong.swap64().long
inline fun Long.swap32() = ulong.swap32().long
inline fun Long.swap16() = ulong.swap16().long

inline fun Int.swap32() = uint.swap32().int
inline fun Int.swap16() = uint.swap16().int

inline fun Short.swap16() = ushort.swap16().short

// =====================================================================================================================
// Bit rotate operations
// =====================================================================================================================

// rotate shift right first value from pair as bits count in second value by amount
inline infix fun Pair<ULong, Int>.rotr(amount: Int): ULong =
    first[amount - 1..0] shl(second - amount) or first[second-1..amount]

inline infix fun ULong.rotr64(amount: Int): ULong = this to 64 rotr amount
inline infix fun ULong.rotr32(amount: Int): ULong = this to 32 rotr amount
inline infix fun ULong.rotr16(amount: Int): ULong = this to 16 rotr amount
inline infix fun ULong.rotr8(amount: Int): ULong = this to 8 rotr amount

inline infix fun Long.rotr64(amount: Int) = (ulong to 64 rotr amount).long
inline infix fun Long.rotr32(amount: Int) = (ulong to 32 rotr amount).long
inline infix fun Long.rotr16(amount: Int) = (ulong to 16 rotr amount).long
inline infix fun Long.rotr8(amount: Int) = (ulong to 8 rotr amount).long

inline infix fun Int.rotr32(amount: Int) = ((ulong_z to 32) rotr amount).int
inline infix fun Int.rotr16(amount: Int) = ((ulong_z to 16) rotr amount).int
inline infix fun Int.rotr8(amount: Int) = ((ulong_z to 8) rotr amount).int

//Now it works and works faster
infix fun Long.rotl32(amount: Int) = (this shl amount) or (this ushr (32 - amount)) mask 32

// =====================================================================================================================
// Bit complement operations
// =====================================================================================================================

/**
 * Returns java unsigned unary minus representation of value (two's complement notation)
 *  -1 -> 0xFFFF_FFFF not 0xFFFF_FFFF_FFFF_FFFF
 * -20 -> 0xFFFF_FFE0 not 0xFFFF_FFFF_FFFF_FFE0
 */
inline infix fun Long.cmpl2(bits: Int) = -this mask bits
inline infix fun Int.cmpl2(bits: Int) = -this mask bits

inline infix fun Boolean.cmpl2(bits: Int) = int cmpl2 bits

inline infix fun ULong.cmpl2(bits: Int) = (-long mask bits).ulong
inline infix fun UInt.cmpl2(bits: Int) = (-int mask bits).ulong_z

inline fun cmpl2(bits: Int) = 1 cmpl2 bits

// =====================================================================================================================
// log2/pow2 operations
// =====================================================================================================================

inline fun log2(n: Int): Int {
    if (n <= 0) throw IllegalArgumentException()
    return Int.SIZE_BITS - Integer.numberOfLeadingZeros(n) - 1
}

inline fun pow2(n: Int) = 1uL shl n

// =====================================================================================================================
// Signed extensions operations
// =====================================================================================================================

inline fun ULong.signext(n: Int): ULong {
    val mask = ubitMask64(n)
    val tmp = this and mask
    return if (tmp <= mask ushr 1) tmp else -((inv(this) and mask) + 1u)
}

inline fun Long.signext(n: Int) = ulong.signext(n).long

// =====================================================================================================================
// Overflow check operation
// =====================================================================================================================

inline fun isIntegerOverflow(op1: Int, op2: Int, res: Int): Boolean =
    (op1 < 0 && op2 < 0 && res >= 0) || (op1 > 0 && op2 > 0 && res < 0)