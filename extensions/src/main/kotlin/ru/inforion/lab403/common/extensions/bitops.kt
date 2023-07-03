@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.extensions

import java.math.BigInteger
import kotlin.experimental.inv
import kotlin.math.min

// =====================================================================================================================
// Shift operations
// =====================================================================================================================

inline infix fun Long.ashr(n: Int) = this shr n

inline infix fun Int.ashr(n: Int) = this shr n

// shl should not be converted to byte because i.e. 0xFF shl 16 return 0 in this case
// which may be not obvious at first glance
inline infix fun Short.shl(n: Int) = int_z shl n
inline infix fun Short.ashr(n: Int) = int_s ashr n
inline infix fun Short.ushr(n: Int) = int_z shr n

// shl should not be converted to byte because i.e. 0xFF shl 8 return 0 in this case
// which may be not obvious at first glance
inline infix fun Byte.shl(count: Int) = int_z shl count
inline infix fun Byte.ashr(count: Int) = int_s ashr count
inline infix fun Byte.ushr(count: Int) = int_z ushr count

inline infix fun ULong.ashr(n: Int) = (long shr n).ulong
inline infix fun ULong.ushr(n: Int) = this shr n

inline infix fun UInt.ashr(n: Int) = (int shr n).uint
inline infix fun UInt.ushr(n: Int) = this shr n

// shl should not be converted to byte because i.e. 0xFFu shl 16 return 0 in this case
// which may be not obvious at first glance
inline infix fun UShort.shl(n: Int) = uint_z shl n
inline infix fun UShort.ashr(n: Int) = (int_s ashr n).uint
inline infix fun UShort.ushr(n: Int) = uint_z shr n

// shl should not be converted to byte because i.e. 0xFFu shl 8 return 0 in this case
// which may be not obvious at first glance
inline infix fun UByte.shl(n: Int) = uint_z shl n
inline infix fun UByte.ashr(n: Int) = (int_s ashr n).uint
inline infix fun UByte.ushr(n: Int) = uint_z shr n

inline infix fun BigInteger.ashr(n: Int): BigInteger { throw NotImplementedError("Arithmetic shift isn't implemented") }
inline infix fun BigInteger.ushr(n: Int) = this shr n

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

// =====================================================================================================================
// Bit reverse operations
// =====================================================================================================================

fun UInt.bitrev32(): UInt {
    var x = this
    x = (((x and 0xAAAAAAAAu) ushr 1) or ((x and 0x55555555u) shl 1))
    x = (((x and 0xCCCCCCCCu) ushr 2) or ((x and 0x33333333u) shl 2))
    x = (((x and 0xF0F0F0F0u) ushr 4) or ((x and 0x0F0F0F0Fu) shl 4))
    x = (((x and 0xFF00FF00u) ushr 8) or ((x and 0x00FF00FFu) shl 8))
    return (x ushr 16) or (x shl 16)
}

inline fun ULong.bitrev32() = uint.bitrev32().ulong_z

fun ULong.bitrev64(): ULong {
    val hi = (this ushr 32).uint.bitrev32()
    val lo = uint.bitrev32()
    return lo.ulong_z shl 32 or hi.ulong_z
}

inline fun Int.bitrev32() = uint.bitrev32().int

inline fun Long.bitrev32() = ulong.bitrev32().long

inline fun Long.bitrev64() = ulong.bitrev64().long

// =====================================================================================================================
// Create bit mask operations
// =====================================================================================================================

fun ubitMask32(size: Int): UInt {
    require(size > 0 && size <= UINT_BITS) { "Can't create ubitMask32 with size=$size" }
    return UINT_MAX ushr (UINT_BITS - size)
}

fun ubitMask64(size: Int): ULong {
    require(size > 0 && size <= ULONG_BITS) { "Can't create ubitMask64 with size=$size" }
    return ULONG_MAX ushr (ULONG_BITS - size)
}

fun ubitMask32(range: IntRange) = if (range.last == 0) ubitMask32(range.first + 1) else
    ubitMask32(range.first + 1) and inv(ubitMask32(range.last))

fun ubitMask64(range: IntRange) = if (range.last == 0) ubitMask64(range.first + 1) else
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
fun Number.bext(n: Int): ULong {
    // used in mips lwl, lwr, swl, swr
    val bit = toInt() and 1
    return if (bit == 1) ubitMask64(n) else 0u
}

// =====================================================================================================================
// Bit extract operations
//
// Note: results commonly are Int or UInt for more convenient operations after bit extraction
// =====================================================================================================================

/**
 * Before we did this: `(this ushr low) and ((1uL shl (high - low)) - 1u)`
 * Overflow was possible, so now algorithm is simple: shift left and then right
 */
fun ULong.xbits(high: Int, low: Int): ULong =
    if (low >= 63) 0uL
    else (this shl (63 - min(63, high))) ushr (low + (63 - min(63, high)))

fun UInt.xbits(high: Int, low: Int): UInt =
    if (low >= 31) 0u
    else (this shl (31 - min(31, high))) ushr (low + (31 - min(31, high)))

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

fun insertBit(dst: ULong, value: ULong, index: Int): ULong {
    val ins = value shl index
    val mask = inv(1uL shl index)
    return dst and mask or ins
}

fun insertBit(dst: UInt, value: UInt, index: Int): UInt {
    val ins = value shl index
    val mask = inv(1u shl index)
    return dst and mask or ins
}

inline fun insertBit(dst: Long, value: Long, index: Int): Long = insertBit(dst.ulong, value.ulong, index).long
inline fun insertBit(dst: Int, value: Int, index: Int): Int = insertBit(dst.uint, value.uint, index).int

fun insertField(dst: ULong, src: ULong, range: IntRange) = (dst bzero range) or ((src shl range.last) mask range)
fun insertField(dst: UInt, src: UInt, range: IntRange) = (dst bzero range) or ((src shl range.last) mask range)

inline fun insertField(dst: Long, src: Long, range: IntRange) = insertField(dst.ulong, src.ulong, range).long
inline fun insertField(dst: Int, src: Int, range: IntRange) = insertField(dst.uint, src.uint, range).int


inline fun ULong.insert(value: ULong, index: Int): ULong = insertBit(this, value, index)
inline fun ULong.insert(value: UInt, index: Int): ULong = insert(value.ulong_z, index)
inline fun ULong.insert(value: Int, index: Int): ULong = insert(value.ulong_z, index)
inline fun ULong.insert(value: Boolean, index: Int): ULong = insert(value.ulong, index)

inline fun UInt.insert(value: UInt, index: Int): UInt = insertBit(this, value, index)
inline fun UInt.insert(value: Int, index: Int): UInt = insert(value.uint, index)
inline fun UInt.insert(value: Boolean, index: Int): UInt = insert(value.uint, index)

inline fun ULong.insert(data: ULong, range: IntRange): ULong = insertField(this, data, range)
inline fun UInt.insert(data: UInt, range: IntRange): UInt = insertField(this, data, range)

inline fun Long.insert(value: Long, index: Int): Long = insertBit(ulong, value.ulong, index).long
inline fun Int.insert(value: Int, index: Int): Int = insertBit(uint, value.uint, index).int

inline fun Long.insert(data: Long, range: IntRange): Long = insertField(ulong, data.ulong, range).long
inline fun Int.insert(data: Int, range: IntRange): Int = insertField(uint, data.uint, range).int


inline fun insert(value: ULong, index: Int): ULong = 0uL.insert(value, index)
inline fun insert(value: UInt, index: Int): UInt = 0u.insert(value, index)

inline fun insert(data: ULong, range: IntRange): ULong = 0uL.insert(data, range)
inline fun insert(data: UInt, range: IntRange): UInt = 0u.insert(data, range)

inline fun insert(value: Long, index: Int): Long = insert(value.ulong, index).long
inline fun insert(value: Int, index: Int): Int = insert(value.uint, index).int
inline fun insert(value: Boolean, index: Int): Int = insert(value.uint, index).int

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
fun cat(left: Long, right: Long, at: Int): Long {
    require(at >= 0)
    return (left shl at + 1).insert(right, at..0)
}

fun cat(left: ULong, right: ULong, at: Int): ULong {
    require(at >= 0)
    return (left shl at + 1).insert(right, at..0)
}

/**
 * Concatenate two int values 'left' and 'right' at border 'at'
 * i.e. cat(0xF, 0xA, 3) = 0xFA
 */
fun cat(left: Int, right: Int, at: Int): Int {
    require(at >= 0)
    return (left shl at + 1).insert(right, at..0)
}

// =====================================================================================================================
// Byte swap operations
// =====================================================================================================================

fun ULong.swap64() =
    (this and 0x00000000_000000FFuL shl  56) or
    (this and 0x00000000_0000FF00uL shl  40) or
    (this and 0x00000000_00FF0000uL shl  24) or
    (this and 0x00000000_FF000000uL shl   8) or
    (this and 0x000000FF_00000000uL ushr  8) or
    (this and 0x0000FF00_00000000uL ushr 24) or
    (this and 0x00FF0000_00000000uL ushr 40) or
    (this and 0xFF000000_00000000uL ushr 56)

fun ULong.swap32() =
    (this and 0x0000_00FFuL shl  24) or
    (this and 0x0000_FF00uL shl   8) or
    (this and 0x00FF_0000uL ushr  8) or
    (this and 0xFF00_0000uL ushr 24)

fun ULong.swap16() =
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

fun ULong.rotr(n: Int, size: Int) = (this ushr n) or (this shl (size - n)) mask size
fun UInt.rotr(n: Int, size: Int) = (this ushr n) or (this shl (size - n)) mask size

inline infix fun ULong.rotr64(n: Int) = rotr(n, 64)
inline infix fun ULong.rotr32(n: Int) = rotr(n, 32)
inline infix fun ULong.rotr16(n: Int) = rotr(n, 16)
inline infix fun ULong.rotr8(n: Int) = rotr(n, 8)

inline infix fun UInt.rotr32(n: Int) = rotr(n, 32)
inline infix fun UInt.rotr16(n: Int) = rotr(n, 16)
inline infix fun UInt.rotr8(n: Int) = rotr(n, 8)

inline infix fun UShort.rotr16(n: Int) = uint_z.rotr(n, 16).ushort
inline infix fun UShort.rotr8(n: Int) = uint_z.rotr(n, 8).ushort

inline infix fun UByte.rotr8(n: Int) = uint_z.rotr(n, 8).ubyte

inline infix fun Long.rotr64(n: Int) = (ulong rotr64 n).long
inline infix fun Long.rotr32(n: Int) = (ulong rotr32 n).long
inline infix fun Long.rotr16(n: Int) = (ulong rotr16 n).long
inline infix fun Long.rotr8(n: Int) = (ulong rotr8 n).long

inline infix fun Int.rotr32(n: Int) = (uint rotr32 n).int
inline infix fun Int.rotr16(n: Int) = (uint rotr16 n).int
inline infix fun Int.rotr8(n: Int) = (uint rotr8 n).int

inline infix fun Short.rotr16(n: Int) = (ushort rotr16 n).short
inline infix fun Short.rotr8(n: Int) = (ushort rotr8 n).short

inline infix fun Byte.rotr8(n: Int) = (ubyte rotr8 n).byte

inline fun ULong.rotl(n: Int, size: Int) = (this shl n) or (this ushr (size - n)) mask size
inline fun UInt.rotl(n: Int, size: Int) = (this shl n) or (this ushr (size - n)) mask size

inline infix fun ULong.rotl64(n: Int) = rotl(n, 64)
inline infix fun ULong.rotl32(n: Int) = rotl(n, 32)
inline infix fun ULong.rotl16(n: Int) = rotl(n, 16)
inline infix fun ULong.rotl8(n: Int) = rotl(n, 8)

inline infix fun UInt.rotl32(n: Int) = rotl(n, 32)
inline infix fun UInt.rotl16(n: Int) = rotl(n, 16)
inline infix fun UInt.rotl8(n: Int) = rotl(n, 8)

inline infix fun UShort.rotl16(n: Int) = uint_z.rotl(n, 16).ushort
inline infix fun UShort.rotl8(n: Int) = uint_z.rotl(n, 8).ushort

inline infix fun UByte.rotl8(n: Int) = uint_z.rotl(n, 8).ubyte

inline infix fun Long.rotl64(n: Int) = (ulong rotl64 n).long
inline infix fun Long.rotl32(n: Int) = (ulong rotl32 n).long
inline infix fun Long.rotl16(n: Int) = (ulong rotl16 n).long
inline infix fun Long.rotl8(n: Int) = (ulong rotl8 n).long

inline infix fun Int.rotl32(n: Int) = (uint rotl32 n).int
inline infix fun Int.rotl16(n: Int) = (uint rotl16 n).int
inline infix fun Int.rotl8(n: Int) = (uint rotl8 n).int

inline infix fun Short.rotl16(n: Int) = (ushort rotl16 n).short
inline infix fun Short.rotl8(n: Int) = (ushort rotl8 n).short

inline infix fun Byte.rotl8(n: Int) = (ubyte rotl8 n).byte

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

fun UByte.log2(): Int {
    require(this > 0u) { "Required positive value for log2() but got $this" }
    return UBYTE_BITS - countLeadingZeroBits() - 1
}

fun UShort.log2(): Int {
    require(this > 0u) { "Required positive value for log2() but got $this" }
    return USHORT_BITS - countLeadingZeroBits() - 1
}

fun UInt.log2(): Int {
    require(this > 0u) { "Required positive value for log2() but got $this" }
    return UINT_BITS - countLeadingZeroBits() - 1
}

fun ULong.log2(): Int {
    require(this > 0u) { "Required positive value for log2() but got $this" }
    return ULONG_BITS - countLeadingZeroBits() - 1
}

fun Byte.log2(): Int {
    require(this > 0) { "Required positive value for log2() but got $this" }
    return BYTE_BITS - countLeadingZeroBits() - 1
}

fun Short.log2(): Int {
    require(this > 0) { "Required positive value for log2() but got $this" }
    return SHORT_BITS - countLeadingZeroBits() - 1
}

fun Int.log2(): Int {
    require(this > 0) { "Required positive value for log2() but got $this" }
    return INT_BITS - countLeadingZeroBits() - 1
}

fun Long.log2(): Int {
    require(this > 0) { "Required positive value for log2() but got $this" }
    return LONG_BITS - countLeadingZeroBits() - 1
}

inline fun pow2(n: Int) = 1uL shl n

// =====================================================================================================================
// Signed extensions operations
// =====================================================================================================================

infix fun ULong.signextRenameMeAfter(n: Int) =
    if ((this ushr n).int.truth) ULONG_MAX shl n or this else inv(ULONG_MAX shl n) and this // 64 - n?

infix fun ULong.signext(n: Int): String = throw IllegalStateException("Refactor is in progress")

fun Long.signextRenameMeAfter(n: Int) = ulong.signextRenameMeAfter(n).long
fun Long.signext(n: Int): String = throw IllegalStateException("Refactor is in progress")

infix fun UInt.signextRenameMeAfter(n: Int) =
    if ((this ushr n).int.truth) UINT_MAX shl n or this else inv(UINT_MAX shl n) and this // 32 - n?

infix fun UInt.signext(n: Int): String = throw IllegalStateException("Refactor is in progress")

fun Int.signextRenameMeAfter(n: Int) = uint.signextRenameMeAfter(n).int
fun Int.signext(n: Int): String = throw IllegalStateException("Refactor is in progress")

// =====================================================================================================================
// Overflow check operation
// =====================================================================================================================

fun isIntegerOverflow(op1: Int, op2: Int, res: Int): Boolean =
    (op1 < 0 && op2 < 0 && res >= 0) || (op1 > 0 && op2 > 0 && res < 0)


fun uint16(b: UByte, a: UByte) = (b shl 8) or (a shl 0)
fun uint32(d: UByte, c: UByte, b: UByte, a: UByte) = (d shl 24) or (c shl 16) or (b shl 8) or (a shl 0)
fun uint64(
    h: UByte, g: UByte, f: UByte, e: UByte,
    d: UByte, c: UByte, b: UByte, a: UByte
) = (h shl 56) or (g shl 48) or (f shl 40) or (e shl 32) or (d shl 24) or (c shl 16) or (b shl 8) or (a shl 0)
