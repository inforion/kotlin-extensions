@file:Suppress("NOTHING_TO_INLINE", "unused")

package unsigned.types

import unsigned.internal.*
import unsigned.ranges.*
import java.io.Serializable

@JvmInline
value class ULong @PublishedApi internal constructor(
    @PublishedApi internal val data: Long
) : Comparable<ULong>, Serializable {

    companion object {
        /**
         * A constant holding the minimum value an instance of ULong can have.
         */
        val MIN_VALUE = ULong(0)

        /**
         * A constant holding the maximum value an instance of ULong can have.
         */
        val MAX_VALUE = ULong(-1)

        /**
         * The number of bytes used to represent an instance of ULong in a binary form.
         */
        const val SIZE_BYTES = 8

        /**
         * The number of bits used to represent an instance of ULong in a binary form.
         */
        const val SIZE_BITS = 64
    }

    /**
     * Compares this value with the specified value for order.
     * Returns zero if this value is equal to the specified other value, a negative number if it's less than other,
     * or a positive number if it's greater than other.
     */
    inline operator fun compareTo(other: UByte) = compareTo(other.toULong())

    /**
     * Compares this value with the specified value for order.
     * Returns zero if this value is equal to the specified other value, a negative number if it's less than other,
     * or a positive number if it's greater than other.
     */
    inline operator fun compareTo(other: UShort) = compareTo(other.toULong())

    /**
     * Compares this value with the specified value for order.
     * Returns zero if this value is equal to the specified other value, a negative number if it's less than other,
     * or a positive number if it's greater than other.
     */
    inline operator fun compareTo(other: UInt) = compareTo(other.toULong())

    /**
     * Compares this value with the specified value for order.
     * Returns zero if this value is equal to the specified other value, a negative number if it's less than other,
     * or a positive number if it's greater than other.
     */
    @Suppress("OVERRIDE_BY_INLINE")
    override inline operator fun compareTo(other: ULong) = ulongCompare(data, other.data)

    /** Adds the other value to this value. */
    inline operator fun plus(other: UByte) = plus(other.toULong())
    /** Adds the other value to this value. */
    inline operator fun plus(other: UShort) = plus(other.toULong())
    /** Adds the other value to this value. */
    inline operator fun plus(other: UInt) = plus(other.toULong())
    /** Adds the other value to this value. */
    inline operator fun plus(other: ULong) = ULong(data.plus(other.data))

    /** Subtracts the other value from this value. */
    inline operator fun minus(other: UByte) = minus(other.toULong())
    /** Subtracts the other value from this value. */
    inline operator fun minus(other: UShort) = minus(other.toULong())
    /** Subtracts the other value from this value. */
    inline operator fun minus(other: UInt) = minus(other.toULong())
    /** Subtracts the other value from this value. */
    inline operator fun minus(other: ULong) = ULong(data.minus(other.data))

    /** Multiplies this value by the other value. */
    inline operator fun times(other: UByte) = times(other.toULong())
    /** Multiplies this value by the other value. */
    inline operator fun times(other: UShort) = times(other.toULong())
    /** Multiplies this value by the other value. */
    inline operator fun times(other: UInt) = times(other.toULong())
    /** Multiplies this value by the other value. */
    inline operator fun times(other: ULong) = ULong(data.times(other.data))

    /** Divides this value by the other value, truncating the result to an integer that is closer to zero. */
    inline operator fun div(other: UByte) = div(other.toULong())
    /** Divides this value by the other value, truncating the result to an integer that is closer to zero. */
    inline operator fun div(other: UShort) = div(other.toULong())
    /** Divides this value by the other value, truncating the result to an integer that is closer to zero. */
    inline operator fun div(other: UInt) = div(other.toULong())
    /** Divides this value by the other value, truncating the result to an integer that is closer to zero. */
    inline operator fun div(other: ULong) = ulongDivide(this, other)

    /**
     * Calculates the remainder of truncating division of this value by the other value.
     *
     * The result is always less than the divisor.
     */
    inline operator fun rem(other: UByte) = rem(other.toULong())
    /**
     * Calculates the remainder of truncating division of this value by the other value.
     *
     * The result is always less than the divisor.
     */
    inline operator fun rem(other: UShort) = rem(other.toULong())
    /**
     * Calculates the remainder of truncating division of this value by the other value.
     *
     * The result is always less than the divisor.
     */
    inline operator fun rem(other: UInt) = rem(other.toULong())
    /**
     * Calculates the remainder of truncating division of this value by the other value.
     *
     * The result is always less than the divisor.
     */
    inline operator fun rem(other: ULong) = ulongRemainder(this, other)

    /**
     * Divides this value by the other value, flooring the result to an integer that is closer to negative infinity.
     *
     * For unsigned types, the results of flooring division and truncating division are the same.
     */
    inline fun floorDiv(other: UByte) = floorDiv(other.toULong())
    /**
     * Divides this value by the other value, flooring the result to an integer that is closer to negative infinity.
     *
     * For unsigned types, the results of flooring division and truncating division are the same.
     */
    inline fun floorDiv(other: UShort) = floorDiv(other.toULong())
    /**
     * Divides this value by the other value, flooring the result to an integer that is closer to negative infinity.
     *
     * For unsigned types, the results of flooring division and truncating division are the same.
     */
    inline fun floorDiv(other: UInt) = floorDiv(other.toULong())
    /**
     * Divides this value by the other value, flooring the result to an integer that is closer to negative infinity.
     *
     * For unsigned types, the results of flooring division and truncating division are the same.
     */
    inline fun floorDiv(other: ULong) = div(other)

    /**
     * Calculates the remainder of flooring division of this value by the other value.
     *
     * The result is always less than the divisor.
     *
     * For unsigned types, the remainders of flooring division and truncating division are the same.
     */
    inline fun mod(other: UByte) = mod(other.toULong()).toUByte()
    /**
     * Calculates the remainder of flooring division of this value by the other value.
     *
     * The result is always less than the divisor.
     *
     * For unsigned types, the remainders of flooring division and truncating division are the same.
     */
    inline fun mod(other: UShort) = mod(other.toULong()).toUShort()
    /**
     * Calculates the remainder of flooring division of this value by the other value.
     *
     * The result is always less than the divisor.
     *
     * For unsigned types, the remainders of flooring division and truncating division are the same.
     */
    inline fun mod(other: UInt) = mod(other.toULong()).toUInt()
    /**
     * Calculates the remainder of flooring division of this value by the other value.
     *
     * The result is always less than the divisor.
     *
     * For unsigned types, the remainders of flooring division and truncating division are the same.
     */
    inline fun mod(other: ULong) = rem(other)

    /**
     * Returns this value incremented by one.
     */
    inline operator fun inc() = ULong(data.inc())

    /**
     * Returns this value decremented by one.
     */
    inline operator fun dec() = ULong(data.dec())

    /** Creates a range from this value to the specified [other] value. */
    inline operator fun rangeTo(other: ULong) = ULongRange(this, other)

    /**
     * Shifts this value left by the [bitCount] number of bits.
     *
     * Note that only the six lowest-order bits of the [bitCount] are used as the shift distance.
     * The shift distance actually used is therefore always in the range `0..63`.
     */
    inline infix fun shl(bitCount: Int) = ULong(data shl bitCount)

    /**
     * Shifts this value right by the [bitCount] number of bits, filling the leftmost bits with zeros.
     *
     * Note that only the six lowest-order bits of the [bitCount] are used as the shift distance.
     * The shift distance actually used is therefore always in the range `0..63`.
     */
    inline infix fun shr(bitCount: Int) = ULong(data ushr bitCount)

    /** Performs a bitwise AND operation between the two values. */
    inline infix fun and(other: ULong) = ULong(data and other.data)
    /** Performs a bitwise OR operation between the two values. */
    inline infix fun or(other: ULong) = ULong(data or other.data)
    /** Performs a bitwise XOR operation between the two values. */
    inline infix fun xor(other: ULong) = ULong(data xor other.data)
    /** Inverts the bits in this value. */
    inline fun inv() = ULong(data.inv())

    /**
     * Converts this [ULong] value to [Byte].
     *
     * If this value is less than or equals to [Byte.MAX_VALUE], the resulting `Byte` value represents
     * the same numerical value as this `ULong`.
     *
     * The resulting `Byte` value is represented by the least significant 8 bits of this `ULong` value.
     * Note that the resulting `Byte` value may be negative.
     */
    inline fun toByte() = data.toByte()
    /**
     * Converts this [ULong] value to [Short].
     *
     * If this value is less than or equals to [Short.MAX_VALUE], the resulting `Short` value represents
     * the same numerical value as this `ULong`.
     *
     * The resulting `Short` value is represented by the least significant 16 bits of this `ULong` value.
     * Note that the resulting `Short` value may be negative.
     */
    inline fun toShort() = data.toShort()
    /**
     * Converts this [ULong] value to [Int].
     *
     * If this value is less than or equals to [Int.MAX_VALUE], the resulting `Int` value represents
     * the same numerical value as this `ULong`.
     *
     * The resulting `Int` value is represented by the least significant 32 bits of this `ULong` value.
     * Note that the resulting `Int` value may be negative.
     */
    inline fun toInt() = data.toInt()
    /**
     * Converts this [ULong] value to [Long].
     *
     * If this value is less than or equals to [Long.MAX_VALUE], the resulting `Long` value represents
     * the same numerical value as this `ULong`. Otherwise the result is negative.
     *
     * The resulting `Long` value has the same binary representation as this `ULong` value.
     */
    inline fun toLong() = data

    /**
     * Converts this [ULong] value to [UByte].
     *
     * If this value is less than or equals to [UByte.MAX_VALUE], the resulting `UByte` value represents
     * the same numerical value as this `ULong`.
     *
     * The resulting `UByte` value is represented by the least significant 8 bits of this `ULong` value.
     */
    inline fun toUByte() = data.toUByte()
    /**
     * Converts this [ULong] value to [UShort].
     *
     * If this value is less than or equals to [UShort.MAX_VALUE], the resulting `UShort` value represents
     * the same numerical value as this `ULong`.
     *
     * The resulting `UShort` value is represented by the least significant 16 bits of this `ULong` value.
     */
    inline fun toUShort() = data.toUShort()
    /**
     * Converts this [ULong] value to [UInt].
     *
     * If this value is less than or equals to [UInt.MAX_VALUE], the resulting `UInt` value represents
     * the same numerical value as this `ULong`.
     *
     * The resulting `UInt` value is represented by the least significant 32 bits of this `ULong` value.
     */
    inline fun toUInt() = data.toUInt()
    /** Returns this value. */
    inline fun toULong() = this

    /**
     * Converts this [ULong] value to [Float].
     *
     * The resulting value is the closest `Float` to this `ULong` value.
     * In case when this `ULong` value is exactly between two `Float`s,
     * the one with zero at least significant bit of mantissa is selected.
     */
    inline fun toFloat() = toDouble().toFloat()
    /**
     * Converts this [ULong] value to [Double].
     *
     * The resulting value is the closest `Double` to this `ULong` value.
     * In case when this `ULong` value is exactly between two `Double`s,
     * the one with zero at least significant bit of mantissa is selected.
     */
    inline fun toDouble() = ulongToDouble(data)

    override fun toString() = ulongToString(data)
}
