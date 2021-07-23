@file:Suppress("NOTHING_TO_INLINE", "unused")

package unsigned.types

import unsigned.interfaces.Unsigned
import unsigned.ranges.*
import java.io.Serializable
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or
import kotlin.experimental.xor


@JvmInline
value class UShort @PublishedApi internal constructor(
    @PublishedApi internal val data: Short
) : Comparable<UShort>, Serializable, Unsigned {

    companion object {
        /**
         * A constant holding the minimum value an instance of UShort can have.
         */
        val MIN_VALUE = UShort(0)

        /**
         * A constant holding the maximum value an instance of UShort can have.
         */
        val MAX_VALUE = UShort(-1)

        /**
         * The number of bytes used to represent an instance of UShort in a binary form.
         */
        const val SIZE_BYTES = 2

        /**
         * The number of bits used to represent an instance of UShort in a binary form.
         */
        const val SIZE_BITS = 16
    }

    /**
     * Compares this value with the specified value for order.
     * Returns zero if this value is equal to the specified other value, a negative number if it's less than other,
     * or a positive number if it's greater than other.
     */
    inline operator fun compareTo(other: UByte) = toInt().compareTo(other.toInt())

    /**
     * Compares this value with the specified value for order.
     * Returns zero if this value is equal to the specified other value, a negative number if it's less than other,
     * or a positive number if it's greater than other.
     */
    @Suppress("OVERRIDE_BY_INLINE")
    override inline operator fun compareTo(other: UShort) = toInt().compareTo(other.toInt())

    /**
     * Compares this value with the specified value for order.
     * Returns zero if this value is equal to the specified other value, a negative number if it's less than other,
     * or a positive number if it's greater than other.
     */
    inline operator fun compareTo(other: UInt) = toUInt().compareTo(other)

    /**
     * Compares this value with the specified value for order.
     * Returns zero if this value is equal to the specified other value, a negative number if it's less than other,
     * or a positive number if it's greater than other.
     */
    inline operator fun compareTo(other: ULong) = toULong().compareTo(other)

    /** Adds the other value to this value. */
    inline operator fun plus(other: UByte) = toUInt().plus(other.toUInt())
    /** Adds the other value to this value. */
    inline operator fun plus(other: UShort) = toUInt().plus(other.toUInt())
    /** Adds the other value to this value. */
    inline operator fun plus(other: UInt) = toUInt().plus(other)
    /** Adds the other value to this value. */
    inline operator fun plus(other: ULong) = toULong().plus(other)

    /** Subtracts the other value from this value. */
    inline operator fun minus(other: UByte) = toUInt().minus(other.toUInt())
    /** Subtracts the other value from this value. */
    inline operator fun minus(other: UShort) = toUInt().minus(other.toUInt())
    /** Subtracts the other value from this value. */
    inline operator fun minus(other: UInt) = toUInt().minus(other)
    /** Subtracts the other value from this value. */
    inline operator fun minus(other: ULong) = toULong().minus(other)

    /** Multiplies this value by the other value. */
    inline operator fun times(other: UByte) = toUInt().times(other.toUInt())
    /** Multiplies this value by the other value. */
    inline operator fun times(other: UShort) = toUInt().times(other.toUInt())
    /** Multiplies this value by the other value. */
    inline operator fun times(other: UInt) = toUInt().times(other)
    /** Multiplies this value by the other value. */
    inline operator fun times(other: ULong) = toULong().times(other)

    /** Divides this value by the other value, truncating the result to an integer that is closer to zero. */
    inline operator fun div(other: UByte) = toUInt().div(other.toUInt())
    /** Divides this value by the other value, truncating the result to an integer that is closer to zero. */
    inline operator fun div(other: UShort) = toUInt().div(other.toUInt())
    /** Divides this value by the other value, truncating the result to an integer that is closer to zero. */
    inline operator fun div(other: UInt) = toUInt().div(other)
    /** Divides this value by the other value, truncating the result to an integer that is closer to zero. */
    inline operator fun div(other: ULong) = toULong().div(other)

    /**
     * Calculates the remainder of truncating division of this value by the other value.
     *
     * The result is always less than the divisor.
     */
    inline operator fun rem(other: UByte) = toUInt().rem(other.toUInt())
    /**
     * Calculates the remainder of truncating division of this value by the other value.
     *
     * The result is always less than the divisor.
     */
    inline operator fun rem(other: UShort) = toUInt().rem(other.toUInt())
    /**
     * Calculates the remainder of truncating division of this value by the other value.
     *
     * The result is always less than the divisor.
     */
    inline operator fun rem(other: UInt) = toUInt().rem(other)
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
    inline fun floorDiv(other: UByte) = toUInt().floorDiv(other.toUInt())
    /**
     * Divides this value by the other value, flooring the result to an integer that is closer to negative infinity.
     *
     * For unsigned types, the results of flooring division and truncating division are the same.
     */
    inline fun floorDiv(other: UShort) = toUInt().floorDiv(other.toUInt())
    /**
     * Divides this value by the other value, flooring the result to an integer that is closer to negative infinity.
     *
     * For unsigned types, the results of flooring division and truncating division are the same.
     */
    inline fun floorDiv(other: UInt) = toUInt().floorDiv(other)
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
    inline fun mod(other: UByte) = toUInt().mod(other.toUInt()).toUByte()
    /**
     * Calculates the remainder of flooring division of this value by the other value.
     *
     * The result is always less than the divisor.
     *
     * For unsigned types, the remainders of flooring division and truncating division are the same.
     */
    inline fun mod(other: UShort) = toUInt().mod(other.toUInt()).toUShort()
    /**
     * Calculates the remainder of flooring division of this value by the other value.
     *
     * The result is always less than the divisor.
     *
     * For unsigned types, the remainders of flooring division and truncating division are the same.
     */
    inline fun mod(other: UInt) = toUInt().mod(other)
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
    inline operator fun inc() = UShort(data.inc())

    /**
     * Returns this value decremented by one.
     */
    inline operator fun dec() = UShort(data.dec())

    /** Creates a range from this value to the specified [other] value. */
    inline operator fun rangeTo(other: UShort): UIntRange = UIntRange(toUInt(), other.toUInt())

    /** Performs a bitwise AND operation between the two values. */
    inline infix fun and(other: UShort) = UShort(data and other.data)
    /** Performs a bitwise OR operation between the two values. */
    inline infix fun or(other: UShort) = UShort(data or other.data)
    /** Performs a bitwise XOR operation between the two values. */
    inline infix fun xor(other: UShort) = UShort(data xor other.data)
    /** Inverts the bits in this value. */
    inline fun inv() = UShort(data.inv())

    /**
     * Converts this [UShort] value to [Byte].
     *
     * If this value is less than or equals to [Byte.MAX_VALUE], the resulting `Byte` value represents
     * the same numerical value as this `UShort`.
     *
     * The resulting `Byte` value is represented by the least significant 8 bits of this `UShort` value.
     * Note that the resulting `Byte` value may be negative.
     */
    inline fun toByte() = data.toByte()
    /**
     * Converts this [UShort] value to [Short].
     *
     * If this value is less than or equals to [Short.MAX_VALUE], the resulting `Short` value represents
     * the same numerical value as this `UShort`. Otherwise the result is negative.
     *
     * The resulting `Short` value has the same binary representation as this `UShort` value.
     */
    inline fun toShort() = data
    /**
     * Converts this [UShort] value to [Int].
     *
     * The resulting `Int` value represents the same numerical value as this `UShort`.
     *
     * The least significant 16 bits of the resulting `Int` value are the same as the bits of this `UShort` value,
     * whereas the most significant 16 bits are filled with zeros.
     */
    inline fun toInt() = data.toInt() and 0xFFFF
    /**
     * Converts this [UShort] value to [Long].
     *
     * The resulting `Long` value represents the same numerical value as this `UShort`.
     *
     * The least significant 16 bits of the resulting `Long` value are the same as the bits of this `UShort` value,
     * whereas the most significant 48 bits are filled with zeros.
     */
    inline fun toLong() = data.toLong() and 0xFFFF

    /**
     * Converts this [UShort] value to [UByte].
     *
     * If this value is less than or equals to [UByte.MAX_VALUE], the resulting `UByte` value represents
     * the same numerical value as this `UShort`.
     *
     * The resulting `UByte` value is represented by the least significant 8 bits of this `UShort` value.
     */
    inline fun toUByte() = data.toUByte()
    /** Returns this value. */
    inline fun toUShort() = this
    /**
     * Converts this [UShort] value to [UInt].
     *
     * The resulting `UInt` value represents the same numerical value as this `UShort`.
     *
     * The least significant 16 bits of the resulting `UInt` value are the same as the bits of this `UShort` value,
     * whereas the most significant 16 bits are filled with zeros.
     */
    inline fun toUInt() = UInt(data.toInt() and 0xFFFF)
    /**
     * Converts this [UShort] value to [ULong].
     *
     * The resulting `ULong` value represents the same numerical value as this `UShort`.
     *
     * The least significant 16 bits of the resulting `ULong` value are the same as the bits of this `UShort` value,
     * whereas the most significant 48 bits are filled with zeros.
     */
    inline fun toULong() = ULong(data.toLong() and 0xFFFF)

    /**
     * Converts this [UShort] value to [Float].
     *
     * The resulting `Float` value represents the same numerical value as this `UShort`.
     */
    inline fun toFloat(): Float = toInt().toFloat()
    /**
     * Converts this [UShort] value to [Double].
     *
     * The resulting `Double` value represents the same numerical value as this `UShort`.
     */
    inline fun toDouble(): Double = toInt().toDouble()

    override fun toString(): String = toInt().toString()

}