@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.extensions

import kotlin.experimental.and
import kotlin.experimental.xor

inline fun bitMask(size: Int): Long {
    require(size in 1..64) { "Size must be in range 1..64" }
    return -1L ushr (64 - size)
}

inline fun bitMask(range: IntRange): Long = if (range.last == 0) bitMask(range.first + 1) else
    bitMask(range.first + 1) and bitMask(range.last).inv()

inline infix fun Long.mask(size: Int): Long = this and bitMask(size)
inline infix fun Int.mask(size: Int): Int = this and bitMask(size).toInt()
inline infix fun Short.mask(size: Int): Short = this and bitMask(size).asShort
inline infix fun Byte.mask(size: Int): Byte = this and bitMask(size).asByte

/**
 * Fill with zeros bit outside the specified range for long values
 */
inline infix fun Long.mask(range: IntRange): Long = this and bitMask(range)
inline infix fun Int.mask(range: IntRange): Int = this and bitMask(range).toInt()
inline infix fun Short.mask(range: IntRange): Short = (asULong and bitMask(range)).asShort
inline infix fun Byte.mask(range: IntRange): Byte = (asULong and bitMask(range)).asByte

/**
 * Fill with zero specified bit range (from msb to lsb)
 */
inline infix fun Long.bzero(range: IntRange): Long = this and bitMask(range).inv()
inline infix fun Int.bzero(range: IntRange): Int = this and bitMask(range).inv().toInt()
inline infix fun Short.bzero(range: IntRange): Short = (asULong and bitMask(range).inv()).asShort
inline infix fun Byte.bzero(range: IntRange): Byte = (asULong and bitMask(range).inv()).asByte


inline fun signext(value: Long, n: Int): Int {
    val mask = bitMask(n)
    val result = if (value and mask > mask shr 1) {
        -((value.inv() and mask) + 1)
    } else {
        value and mask
    }
    return result.asInt
}

/**
 * Make bit extension by the lowest bit of the id.
 * for 0x034251.bext(3) return 0b111
 * for 0x031400.bext(10) return 0
 */
inline fun Number.bext(n: Int): Long {
    val bit = toInt() and 0x1
    return if (bit == 1) bitMask(n) else 0L
}

// To unsigned long conversion
inline fun Double.toULong(): Long = asLong and INT32MASK
inline fun Float.toULong(): Long = asLong and INT32MASK
inline fun Int.toULong(): Long = asLong and INT32MASK

inline fun Short.toULong(): Long = asLong and INT16MASK.asULong
inline fun Short.toUInt(): Int = toInt() and INT16MASK

inline fun Byte.toULong(): Long = asLong and INT8MASK.asULong
inline fun Byte.toUInt(): Int = toInt() and INT8MASK
inline fun Char.toULong(): Long = asLong and INT8MASK.asULong
inline fun Char.toUInt(): Int = toInt() and INT8MASK

// Get one many bits
inline fun Long.xbits(high: Int, low: Int): Long = (this shr low) and ((1L shl (high - low + 1)) - 1)
inline fun Int.xbits(high: Int, low: Int): Int = (this shr low) and ((1 shl (high - low + 1)) - 1)

inline operator fun Long.get(range: IntRange): Long = xbits(range.first, range.last)
inline operator fun Int.get(range: IntRange): Int = xbits(range.first, range.last)
inline operator fun Short.get(range: IntRange): Short = toInt().xbits(range.first, range.last).asShort
inline operator fun Byte.get(range: IntRange): Byte = toInt().xbits(range.first, range.last).asByte

// Get one bit methods
inline fun Long.xbit(indx: Int): Long = (this shr indx) and 1
inline fun Int.xbit(indx: Int): Int = (this shr indx) and 1

inline operator fun Long.get(indx: Int): Long = xbit(indx)
inline operator fun Int.get(indx: Int): Int = xbit(indx)
inline operator fun Short.get(indx: Int): Short = toInt().xbit(indx).asShort
inline operator fun Byte.get(indx: Int): Byte = toInt().xbit(indx).asByte

// Set one bit
inline fun insertBit(dst: Long, value: Int, indx: Int): Long {
    val ins = value.asLong.shl(indx)
    val mask = 1L.shl(indx).inv()
    return dst.and(mask).or(ins)
}

inline fun insertBit(dst: Int, value: Int, indx: Int): Int {
    val ins = value.shl(indx)
    val mask = (1 shl indx).inv()
    return dst.and(mask).or(ins)
}


inline fun clearBit(dst: Long, indx: Int): Long = dst and (1L shl indx).inv()
inline fun clearBit(dst: Int, indx: Int): Int = dst and (1 shl indx).inv()
inline fun clearBit(dst: Short, indx: Int): Short = (dst.toUInt() and (1 shl indx).inv()).asShort

inline fun setBit(dst: Long, indx: Int): Long = dst or (1L shl indx)
inline fun setBit(dst: Int, indx: Int): Int = dst or (1 shl indx)
inline fun setBit(dst: Short, indx: Int): Short = (dst.toUInt() or (1 shl indx)).asShort

inline fun toggleBit(dst: Long, indx: Int): Long = dst xor (1L shl indx)
inline fun toggleBit(dst: Int, indx: Int): Int = dst xor (1 shl indx)
inline fun toggleBit(dst: Short, indx: Int): Short = (dst.toUInt() xor (1 shl indx)).asShort

inline infix fun Long.toggle(index: Int): Long = toggleBit(this, index)
inline infix fun Int.toggle(index: Int): Int = toggleBit(this, index)
inline infix fun Short.toggle(index: Int): Short = toggleBit(this, index)

inline infix fun Long.clr(index: Int): Long = clearBit(this, index)
inline infix fun Int.clr(index: Int): Int = clearBit(this, index)
inline infix fun Short.clr(index: Int): Short = clearBit(this, index)

inline infix fun Long.clr(range: IntRange): Long = this bzero range
inline infix fun Int.clr(range: IntRange): Int = this bzero range
inline infix fun Short.clr(range: IntRange): Short = this bzero range

inline infix fun Long.set(index: Int): Long = setBit(this, index)
inline infix fun Int.set(index: Int): Int = setBit(this, index)
inline infix fun Short.set(index: Int): Short = setBit(this, index)

inline fun insertField(dst: Long, src: Long, range: IntRange): Long = (dst bzero range) or ((src shl range.last) mask range)
inline fun insertField(dst: Int, src: Int, range: IntRange): Int = (dst bzero range) or ((src shl range.last) mask range)

inline fun isIntegerOverflow(op1: Int, op2: Int, res: Int): Boolean =
        (op1 < 0 && op2 < 0 && res >= 0) || (op1 > 0 && op2 > 0 && res < 0)

inline fun Long.insert(value: Long, indx: Int): Long = insertBit(this, value.asInt, indx)
inline fun Long.insert(value: Int, indx: Int): Long = insertBit(this, value, indx)
inline fun Int.insert(value: Int, indx: Int): Int = insertBit(this, value, indx)

inline fun Long.insert(data: Long, range: IntRange): Long = insertField(this, data, range)
inline fun Int.insert(data: Int, range: IntRange): Int = insertField(this, data, range)

/**
 * Concatenate two long values 'left' and 'right' at border 'at'
 * i.e. cat(0xFL, 0xAL, 3) = 0xFAL
 */
inline fun cat(left: Long, right: Long, at: Int): Long {
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

inline fun insert(value: Int, indx: Int): Int = 0.insert(value, indx)
inline fun insert(value: Long, indx: Int): Long = 0L.insert(value, indx)

inline fun insert(data: Long, range: IntRange): Long = 0L.insert(data, range)
inline fun insert(data: Int, range: IntRange): Int = 0.insert(data, range)

inline fun Long.swap64(): Long = (
        ((this and (0xFF shl  0)) shl  56) or   // 0x00000000_000000FF
                ((this and (0xFF shl  8)) shl  40) or   // 0x00000000_0000FF00
                ((this and (0xFF shl 16)) shl  24) or   // 0x00000000_00FF0000
                ((this and (0xFF shl 24)) shl   8) or   // 0x00000000_FF000000
                ((this and (0xFF shl 32)) ushr  8) or   // 0x000000FF_00000000
                ((this and (0xFF shl 40)) ushr 24) or   // 0x0000FF00_00000000
                ((this and (0xFF shl 48)) ushr 40) or   // 0x00FF0000_00000000
                ((this and (0xFF shl 56)) ushr 56))     // 0xFF000000_00000000

inline fun Long.swap32(): Long = asInt.swap32().asULong
inline fun Long.swap16(): Long = asInt.swap16().asULong

inline fun Int.swap32(): Int = (((this and 0xFF) shl 24) or
        ((this and 0xFF00) shl 8)) or
        ((this and 0xFF0000) ushr 8) or
        ((asULong and 0xFF000000L) ushr 24).toInt()
inline fun Int.swap16(): Int = (((this and 0xFF) shl 8) or ((this and 0xFF00) ushr 8))

inline fun Short.swap16(): Int = ((asUInt and 0xFF) shl 8) or ((asUInt and 0xFF00) ushr 8)

inline infix fun Byte.shl(count: Int): Long = asULong shl count
inline infix fun Byte.shr(count: Int): Long = asULong shr count
inline infix fun Byte.ushr(count: Int): Long = asULong ushr count

inline fun Int.bitReverse(): Int {
    var x = asULong
    x = (((x and 0xaaaaaaaaL) ushr 1) or ((x and 0x55555555) shl 1))
    x = (((x and 0xccccccccL) ushr 2) or ((x and 0x33333333) shl 2))
    x = (((x and 0xf0f0f0f0L) ushr 4) or ((x and 0x0f0f0f0f) shl 4))
    x = (((x and 0xff00ff00L) ushr 8) or ((x and 0x00ff00ff) shl 8))
    return ((x ushr 16) or (x shl 16)).toInt()
}

inline operator fun Byte.not(): Byte = (this and 1) xor 1
inline operator fun Short.not(): Short = (this and 1) xor 1
inline operator fun Int.not(): Int = (this and 1) xor 1
inline operator fun Long.not(): Long = (this and 1) xor 1

// rotate shift right first value from pair as bits count in second value by amount
inline infix fun Pair<Long, Int>.rotr(amount: Int): Long =
        first[amount - 1..0] shl(second - amount) or first[second-1..amount]

inline infix fun Long.rotr64(amount: Int): Long = (this to 64) rotr amount
inline infix fun Long.rotr32(amount: Int): Long = (this to 32) rotr amount
inline infix fun Long.rotr16(amount: Int): Long = (this to 16) rotr amount
inline infix fun Long.rotr8(amount: Int): Long = (this to 8) rotr amount
inline infix fun Int.rotr32(amount: Int): Int = ((asULong to 32) rotr amount).asInt
inline infix fun Int.rotr16(amount: Int): Int = ((asULong to 16) rotr amount).asInt
inline infix fun Int.rotr8(amount: Int): Int = ((asULong to 8) rotr amount).asInt

/**
 * Returns java unsigned unary minus representation of value (two's complement notation)
 *  -1 -> 0xFFFF_FFFF not 0xFFFF_FFFF_FFFF_FFFF
 * -20 -> 0xFFFF_FFE0 not 0xFFFF_FFFF_FFFF_FFE0
 */
inline infix fun Long.cmpl2(bits: Int): Long = -this mask bits
inline infix fun Int.cmpl2(bits: Int): Long = (-this mask bits).asULong
inline infix fun Boolean.cmpl2(bits: Int): Long = asInt cmpl2 bits
inline fun cmpl2(bits: Int): Long = 1.cmpl2(bits)


inline fun log2(n: Int): Int {
    if (n <= 0) throw IllegalArgumentException()
    return 31 - Integer.numberOfLeadingZeros(n)
}

inline fun pow2(n: Int): Long = 1L shl n


/**
 * Sign extension to 32-bit integer
 *
 * @param sbit sign bit
 *
 * @return extended integer
 */
infix fun Long.ssext(sbit: Int) = signext(this, sbit + 1).asLong
infix fun Long.usext(sbit: Int) = signext(this, sbit + 1).asULong

//Now it works and works faster
infix fun Long.rotl32(amount: Int): Long = ((this shl amount) or (this shr (32 - amount))) mask 32

fun Long.replace(index: Int, value: Long): Long = (this and 1L.shl(index).inv() or (value shl index))
fun Long.replace(index: Int, value: Boolean): Long = replace(index, value.asLong)
fun Long.replace(range: IntRange, value: Long): Long = (this and bitMask(range).inv() or (value shl range.last))
