@file:Suppress("NOTHING_TO_INLINE", "unused")

package unsigned.types

import unsigned.interfaces.Unsigned
import unsigned.internal.*
import unsigned.ranges.*
import java.io.Serializable

@JvmInline
value class UInt @PublishedApi internal constructor(
    @PublishedApi internal val data: Int
) : Comparable<UInt>, Serializable, Unsigned {

    companion object {
        /**
         * A constant holding the minimum value an instance of UInt can have.
         */
        val MIN_VALUE: UInt = UInt(0)

        /**
         * A constant holding the maximum value an instance of UInt can have.
         */
        val MAX_VALUE: UInt = UInt(-1)

        /**
         * The number of bytes used to represent an instance of UInt in a binary form.
         */
        const val SIZE_BYTES: Int = 4

        /**
         * The number of bits used to represent an instance of UInt in a binary form.
         */
        const val SIZE_BITS: Int = 32
    }

    /**
     * Compares this value with the specified value for order.
     * Returns zero if this value is equal to the specified other value, a negative number if it's less than other,
     * or a positive number if it's greater than other.
     */
    inline operator fun compareTo(other: UByte) = compareTo(other.toUInt())

    /**
     * Compares this value with the specified value for order.
     * Returns zero if this value is equal to the specified other value, a negative number if it's less than other,
     * or a positive number if it's greater than other.
     */
    inline operator fun compareTo(other: UShort) = compareTo(other.toUInt())

    /**
     * Compares this value with the specified value for order.
     * Returns zero if this value is equal to the specified other value, a negative number if it's less than other,
     * or a positive number if it's greater than other.
     */
    @Suppress("OVERRIDE_BY_INLINE")
    override inline operator fun compareTo(other: UInt) = uintCompare(data, other.data)

    /**
     * Compares this value with the specified value for order.
     * Returns zero if this value is equal to the specified other value, a negative number if it's less than other,
     * or a positive number if it's greater than other.
     */
    inline operator fun compareTo(other: ULong) = toULong().compareTo(other)

    /** Adds the other value to this value. */
    inline operator fun plus(other: UByte) = plus(other.toUInt())
    /** Adds the other value to this value. */
    inline operator fun plus(other: UShort) = plus(other.toUInt())
    /** Adds the other value to this value. */
    inline operator fun plus(other: UInt) = UInt(data.plus(other.data))
    /** Adds the other value to this value. */
    inline operator fun plus(other: ULong) = toULong().plus(other)

    /** Subtracts the other value from this value. */
    inline operator fun minus(other: UByte) = minus(other.toUInt())
    /** Subtracts the other value from this value. */
    inline operator fun minus(other: UShort) = minus(other.toUInt())
    /** Subtracts the other value from this value. */
    inline operator fun minus(other: UInt) = UInt(this.data.minus(other.data))
    /** Subtracts the other value from this value. */
    inline operator fun minus(other: ULong) = toULong().minus(other)

    /** Multiplies this value by the other value. */
    inline operator fun times(other: UByte) = times(other.toUInt())
    /** Multiplies this value by the other value. */
    inline operator fun times(other: UShort) = times(other.toUInt())
    /** Multiplies this value by the other value. */
    inline operator fun times(other: UInt) = UInt(data.times(other.data))
    /** Multiplies this value by the other value. */
    inline operator fun times(other: ULong) = toULong().times(other)

    /** Divides this value by the other value, truncating the result to an integer that is closer to zero. */
    inline operator fun div(other: UByte) = div(other.toUInt())
    /** Divides this value by the other value, truncating the result to an integer that is closer to zero. */
    inline operator fun div(other: UShort) = div(other.toUInt())
    /** Divides this value by the other value, truncating the result to an integer that is closer to zero. */
    inline operator fun div(other: UInt) = uintDivide(this, other)
    /** Divides this value by the other value, truncating the result to an integer that is closer to zero. */
    inline operator fun div(other: ULong) = toULong().div(other)

    /**
     * Calculates the remainder of truncating division of this value by the other value.
     *
     * The result is always less than the divisor.
     */
    inline operator fun rem(other: UByte) = rem(other.toUInt())
    /**
     * Calculates the remainder of truncating division of this value by the other value.
     *
     * The result is always less than the divisor.
     */
    inline operator fun rem(other: UShort) = rem(other.toUInt())
    /**
     * Calculates the remainder of truncating division of this value by the other value.
     *
     * The result is always less than the divisor.
     */
    inline operator fun rem(other: UInt) = uintRemainder(this, other)
    /**
     * Calculates the remainder of truncating division of this value by the other value.
     *
     * The result is always less than the divisor.
     */
    inline operator fun rem(other: ULong) = toULong().rem(other)

    /**
     * Divides this value by the other value, flooring the result to an integer that is closer to negative infinity.
     *
     * For unsigned types, the results of flooring division and truncating division are the same.
     */
    inline fun floorDiv(other: UByte) = floorDiv(other.toUInt())
    /**
     * Divides this value by the other value, flooring the result to an integer that is closer to negative infinity.
     *
     * For unsigned types, the results of flooring division and truncating division are the same.
     */
    inline fun floorDiv(other: UShort) = floorDiv(other.toUInt())
    /**
     * Divides this value by the other value, flooring the result to an integer that is closer to negative infinity.
     *
     * For unsigned types, the results of flooring division and truncating division are the same.
     */
    inline fun floorDiv(other: UInt) = div(other)
    /**
     * Divides this value by the other value, flooring the result to an integer that is closer to negative infinity.
     *
     * For unsigned types, the results of flooring division and truncating division are the same.
     */
    inline fun floorDiv(other: ULong) = toULong().floorDiv(other)

    /**
     * Calculates the remainder of flooring division of this value by the other value.
     *
     * The result is always less than the divisor.
     *
     * For unsigned types, the remainders of flooring division and truncating division are the same.
     */
    inline fun mod(other: UByte) = mod(other.toUInt()).toUByte()
    /**
     * Calculates the remainder of flooring division of this value by the other value.
     *
     * The result is always less than the divisor.
     *
     * For unsigned types, the remainders of flooring division and truncating division are the same.
     */
    inline fun mod(other: UShort) = mod(other.toUInt()).toUShort()
    /**
     * Calculates the remainder of flooring division of this value by the other value.
     *
     * The result is always less than the divisor.
     *
     * For unsigned types, the remainders of flooring division and truncating division are the same.
     */
    inline fun mod(other: UInt) = rem(other)
    /**
     * Calculates the remainder of flooring division of this value by the other value.
     *
     * The result is always less than the divisor.
     *
     * For unsigned types, the remainders of flooring division and truncating division are the same.
     */
    inline fun mod(other: ULong) = toULong().mod(other)

    /**
     * Returns this value incremented by one.
     */
    inline operator fun inc() = UInt(data.inc())

    /**
     * Returns this value decremented by one.
     */
    inline operator fun dec() = UInt(data.dec())

    /** Creates a range from this value to the specified [other] value. */
    inline operator fun rangeTo(other: UInt) = UIntRange(this, other)

    /**
     * Shifts this value left by the [bitCount] number of bits.
     *
     * Note that only the five lowest-order bits of the [bitCount] are used as the shift distance.
     * The shift distance actually used is therefore always in the range `0..31`.
     */
    inline infix fun shl(bitCount: Int) = UInt(data shl bitCount)

    /**
     * Shifts this value right by the [bitCount] number of bits, filling the leftmost bits with zeros.
     *
     * Note that only the five lowest-order bits of the [bitCount] are used as the shift distance.
     * The shift distance actually used is therefore always in the range `0..31`.
     */
    inline infix fun shr(bitCount: Int) = UInt(data ushr bitCount)

    /** Performs a bitwise AND operation between the two values. */
    inline infix fun and(other: UInt) = UInt(data and other.data)
    /** Performs a bitwise OR operation between the two values. */
    inline infix fun or(other: UInt) = UInt(data or other.data)
    /** Performs a bitwise XOR operation between the two values. */
    inline infix fun xor(other: UInt) = UInt(data xor other.data)
    /** Inverts the bits in this value. */
    inline fun inv() = UInt(data.inv())

    /**
     * Converts this [UInt] value to [Byte].
     *
     * If this value is less than or equals to [Byte.MAX_VALUE], the resulting `Byte` value represents
     * the same numerical value as this `UInt`.
     *
     * The resulting `Byte` value is represented by the least significant 8 bits of this `UInt` value.
     * Note that the resulting `Byte` value may be negative.
     */
    inline fun toByte() = data.toByte()
    /**
     * Converts this [UInt] value to [Short].
     *
     * If this value is less than or equals to [Short.MAX_VALUE], the resulting `Short` value represents
     * the same numerical value as this `UInt`.
     *
     * The resulting `Short` value is represented by the least significant 16 bits of this `UInt` value.
     * Note that the resulting `Short` value may be negative.
     */
    inline fun toShort() = data.toShort()
    /**
     * Converts this [UInt] value to [Int].
     *
     * If this value is less than or equals to [Int.MAX_VALUE], the resulting `Int` value represents
     * the same numerical value as this `UInt`. Otherwise the result is negative.
     *
     * The resulting `Int` value has the same binary representation as this `UInt` value.
     */
    inline fun toInt() = data
    /**
     * Converts this [UInt] value to [Long].
     *
     * The resulting `Long` value represents the same numerical value as this `UInt`.
     *
     * The least significant 32 bits of the resulting `Long` value are the same as the bits of this `UInt` value,
     * whereas the most significant 32 bits are filled with zeros.
     */
    inline fun toLong() = data.toLong() and 0xFFFF_FFFF

    /**
     * Converts this [UInt] value to [UByte].
     *
     * If this value is less than or equals to [UByte.MAX_VALUE], the resulting `UByte` value represents
     * the same numerical value as this `UInt`.
     *
     * The resulting `UByte` value is represented by the least significant 8 bits of this `UInt` value.
     */
    inline fun toUByte() = data.toUByte()
    /**
     * Converts this [UInt] value to [UShort].
     *
     * If this value is less than or equals to [UShort.MAX_VALUE], the resulting `UShort` value represents
     * the same numerical value as this `UInt`.
     *
     * The resulting `UShort` value is represented by the least significant 16 bits of this `UInt` value.
     */
    inline fun toUShort() = data.toUShort()
    /** Returns this value. */
    inline fun toUInt() = this
    /**
     * Converts this [UInt] value to [ULong].
     *
     * The resulting `ULong` value represents the same numerical value as this `UInt`.
     *
     * The least significant 32 bits of the resulting `ULong` value are the same as the bits of this `UInt` value,
     * whereas the most significant 32 bits are filled with zeros.
     */
    inline fun toULong() = ULong(data.toLong() and 0xFFFF_FFFF)

    /**
     * Converts this [UInt] value to [Float].
     *
     * The resulting value is the closest `Float` to this `UInt` value.
     * In case when this `UInt` value is exactly between two `Float`s,
     * the one with zero at least significant bit of mantissa is selected.
     */
    inline fun toFloat() = toDouble().toFloat()
    /**
     * Converts this [UInt] value to [Double].
     *
     * The resulting `Double` value represents the same numerical value as this `UInt`.
     */
    inline fun toDouble() = uintToDouble(data)

    override fun toString() = data.toString()
}
