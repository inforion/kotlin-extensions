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
value class UByte @PublishedApi internal constructor(
    @PublishedApi internal val data: Byte
) : Comparable<UByte>, Serializable, Unsigned {

    companion object {
        /**
         * A constant holding the minimum value an instance of UByte can have.
         */
        val MIN_VALUE = UByte(0)

        /**
         * A constant holding the maximum value an instance of UByte can have.
         */
        val MAX_VALUE = UByte(-1)

        /**
         * The number of bytes used to represent an instance of UByte in a binary form.
         */
        const val SIZE_BYTES = 1

        /**
         * The number of bits used to represent an instance of UByte in a binary form.
         */
        const val SIZE_BITS = 8
    }

    /**
     * Compares this value with the specified value for order.
     * Returns zero if this value is equal to the specified other value, a negative number if it's less than other,
     * or a positive number if it's greater than other.
     */
    @Suppress("OVERRIDE_BY_INLINE")
    override inline operator fun compareTo(other: UByte) = toInt().compareTo(other.toInt())

    /**
     * Compares this value with the specified value for order.
     * Returns zero if this value is equal to the specified other value, a negative number if it's less than other,
     * or a positive number if it's greater than other.
     */
    inline operator fun compareTo(other: UShort) = toInt().compareTo(other.toInt())

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
    inline operator fun inc() = UByte(data.inc())

    /**
     * Returns this value decremented by one.
     */
    inline operator fun dec() = UByte(data.dec())

    /** Creates a range from this value to the specified [other] value. */
    inline operator fun rangeTo(other: UByte) = UIntRange(toUInt(), other.toUInt())

    /** Performs a bitwise AND operation between the two values. */
    inline infix fun and(other: UByte) = UByte(data and other.data)
    /** Performs a bitwise OR operation between the two values. */
    inline infix fun or(other: UByte) = UByte(data or other.data)
    /** Performs a bitwise XOR operation between the two values. */
    inline infix fun xor(other: UByte) = UByte(data xor other.data)
    /** Inverts the bits in this value. */
    inline fun inv() = UByte(data.inv())

    /**
     * Converts this [UByte] value to [Byte].
     *
     * If this value is less than or equals to [Byte.MAX_VALUE], the resulting `Byte` value represents
     * the same numerical value as this `UByte`. Otherwise the result is negative.
     *
     * The resulting `Byte` value has the same binary representation as this `UByte` value.
     */
    inline fun toByte() = data
    /**
     * Converts this [UByte] value to [Short].
     *
     * The resulting `Short` value represents the same numerical value as this `UByte`.
     *
     * The least significant 8 bits of the resulting `Short` value are the same as the bits of this `UByte` value,
     * whereas the most significant 8 bits are filled with zeros.
     */
    inline fun toShort() = data.toShort() and 0xFF
    /**
     * Converts this [UByte] value to [Int].
     *
     * The resulting `Int` value represents the same numerical value as this `UByte`.
     *
     * The least significant 8 bits of the resulting `Int` value are the same as the bits of this `UByte` value,
     * whereas the most significant 24 bits are filled with zeros.
     */
    inline fun toInt() = data.toInt() and 0xFF
    /**
     * Converts this [UByte] value to [Long].
     *
     * The resulting `Long` value represents the same numerical value as this `UByte`.
     *
     * The least significant 8 bits of the resulting `Long` value are the same as the bits of this `UByte` value,
     * whereas the most significant 56 bits are filled with zeros.
     */
    inline fun toLong() = data.toLong() and 0xFF

    /** Returns this value. */
    inline fun toUByte() = this
    /**
     * Converts this [UByte] value to [UShort].
     *
     * The resulting `UShort` value represents the same numerical value as this `UByte`.
     *
     * The least significant 8 bits of the resulting `UShort` value are the same as the bits of this `UByte` value,
     * whereas the most significant 8 bits are filled with zeros.
     */
    inline fun toUShort() = UShort(data.toShort() and 0xFF)
    /**
     * Converts this [UByte] value to [UInt].
     *
     * The resulting `UInt` value represents the same numerical value as this `UByte`.
     *
     * The least significant 8 bits of the resulting `UInt` value are the same as the bits of this `UByte` value,
     * whereas the most significant 24 bits are filled with zeros.
     */
    inline fun toUInt() = UInt(data.toInt() and 0xFF)
    /**
     * Converts this [UByte] value to [ULong].
     *
     * The resulting `ULong` value represents the same numerical value as this `UByte`.
     *
     * The least significant 8 bits of the resulting `ULong` value are the same as the bits of this `UByte` value,
     * whereas the most significant 56 bits are filled with zeros.
     */
    inline fun toULong() = ULong(data.toLong() and 0xFF)

    /**
     * Converts this [UByte] value to [Float].
     *
     * The resulting `Float` value represents the same numerical value as this `UByte`.
     */
    inline fun toFloat() = toInt().toFloat()
    /**
     * Converts this [UByte] value to [Double].
     *
     * The resulting `Double` value represents the same numerical value as this `UByte`.
     */
    inline fun toDouble() = toInt().toDouble()

    override fun toString() = toInt().toString()

}
