@file:Suppress("NOTHING_TO_INLINE", "unused")

package unsigned.types

import unsigned.internal.*


/**
 * Converts this [Byte] value to [UByte].
 *
 * If this value is positive, the resulting `UByte` value represents the same numerical value as this `Byte`.
 *
 * The resulting `UByte` value has the same binary representation as this `Byte` value.
 */
inline fun Byte.toUByte() = UByte(this)
/**
 * Converts this [Short] value to [UByte].
 *
 * If this value is positive and less than or equals to [UByte.MAX_VALUE], the resulting `UByte` value represents
 * the same numerical value as this `Short`.
 *
 * The resulting `UByte` value is represented by the least significant 8 bits of this `Short` value.
 */
inline fun Short.toUByte() = UByte(toByte())
/**
 * Converts this [Int] value to [UByte].
 *
 * If this value is positive and less than or equals to [UByte.MAX_VALUE], the resulting `UByte` value represents
 * the same numerical value as this `Int`.
 *
 * The resulting `UByte` value is represented by the least significant 8 bits of this `Int` value.
 */
inline fun Int.toUByte() = UByte(toByte())
/**
 * Converts this [Long] value to [UByte].
 *
 * If this value is positive and less than or equals to [UByte.MAX_VALUE], the resulting `UByte` value represents
 * the same numerical value as this `Long`.
 *
 * The resulting `UByte` value is represented by the least significant 8 bits of this `Long` value.
 */
inline fun Long.toUByte() = UByte(toByte())




/**
 * Converts this [Byte] value to [UShort].
 *
 * If this value is positive, the resulting `UShort` value represents the same numerical value as this `Byte`.
 *
 * The least significant 8 bits of the resulting `UShort` value are the same as the bits of this `Byte` value,
 * whereas the most significant 8 bits are filled with the sign bit of this value.
 */
inline fun Byte.toUShort() = UShort(toShort())
/**
 * Converts this [Short] value to [UShort].
 *
 * If this value is positive, the resulting `UShort` value represents the same numerical value as this `Short`.
 *
 * The resulting `UShort` value has the same binary representation as this `Short` value.
 */
inline fun Short.toUShort() = UShort(this)
/**
 * Converts this [Int] value to [UShort].
 *
 * If this value is positive and less than or equals to [UShort.MAX_VALUE], the resulting `UShort` value represents
 * the same numerical value as this `Int`.
 *
 * The resulting `UShort` value is represented by the least significant 16 bits of this `Int` value.
 */
inline fun Int.toUShort() = UShort(toShort())
/**
 * Converts this [Long] value to [UShort].
 *
 * If this value is positive and less than or equals to [UShort.MAX_VALUE], the resulting `UShort` value represents
 * the same numerical value as this `Long`.
 *
 * The resulting `UShort` value is represented by the least significant 16 bits of this `Long` value.
 */
inline fun Long.toUShort() = UShort(toShort())




/**
 * Converts this [Byte] value to [UInt].
 *
 * If this value is positive, the resulting `UInt` value represents the same numerical value as this `Byte`.
 *
 * The least significant 8 bits of the resulting `UInt` value are the same as the bits of this `Byte` value,
 * whereas the most significant 24 bits are filled with the sign bit of this value.
 */
inline fun Byte.toUInt(): UInt = UInt(toInt())
/**
 * Converts this [Short] value to [UInt].
 *
 * If this value is positive, the resulting `UInt` value represents the same numerical value as this `Short`.
 *
 * The least significant 16 bits of the resulting `UInt` value are the same as the bits of this `Short` value,
 * whereas the most significant 16 bits are filled with the sign bit of this value.
 */
inline fun Short.toUInt(): UInt = UInt(toInt())
/**
 * Converts this [Int] value to [UInt].
 *
 * If this value is positive, the resulting `UInt` value represents the same numerical value as this `Int`.
 *
 * The resulting `UInt` value has the same binary representation as this `Int` value.
 */
inline fun Int.toUInt(): UInt = UInt(this)
/**
 * Converts this [Long] value to [UInt].
 *
 * If this value is positive and less than or equals to [UInt.MAX_VALUE], the resulting `UInt` value represents
 * the same numerical value as this `Long`.
 *
 * The resulting `UInt` value is represented by the least significant 32 bits of this `Long` value.
 */
inline fun Long.toUInt(): UInt = UInt(toInt())

/**
 * Converts this [Float] value to [UInt].
 *
 * The fractional part, if any, is rounded down towards zero.
 * Returns zero if this `Float` value is negative or `NaN`, [UInt.MAX_VALUE] if it's bigger than `UInt.MAX_VALUE`.
 */
inline fun Float.toUInt(): UInt = doubleToUInt(toDouble())
/**
 * Converts this [Double] value to [UInt].
 *
 * The fractional part, if any, is rounded down towards zero.
 * Returns zero if this `Double` value is negative or `NaN`, [UInt.MAX_VALUE] if it's bigger than `UInt.MAX_VALUE`.
 */
inline fun Double.toUInt(): UInt = doubleToUInt(this)





/**
 * Converts this [Byte] value to [ULong].
 *
 * If this value is positive, the resulting `ULong` value represents the same numerical value as this `Byte`.
 *
 * The least significant 8 bits of the resulting `ULong` value are the same as the bits of this `Byte` value,
 * whereas the most significant 56 bits are filled with the sign bit of this value.
 */
inline fun Byte.toULong() = ULong(toLong())
/**
 * Converts this [Short] value to [ULong].
 *
 * If this value is positive, the resulting `ULong` value represents the same numerical value as this `Short`.
 *
 * The least significant 16 bits of the resulting `ULong` value are the same as the bits of this `Short` value,
 * whereas the most significant 48 bits are filled with the sign bit of this value.
 */
inline fun Short.toULong() = ULong(toLong())
/**
 * Converts this [Int] value to [ULong].
 *
 * If this value is positive, the resulting `ULong` value represents the same numerical value as this `Int`.
 *
 * The least significant 32 bits of the resulting `ULong` value are the same as the bits of this `Int` value,
 * whereas the most significant 32 bits are filled with the sign bit of this value.
 */
inline fun Int.toULong() = ULong(toLong())
/**
 * Converts this [Long] value to [ULong].
 *
 * If this value is positive, the resulting `ULong` value represents the same numerical value as this `Long`.
 *
 * The resulting `ULong` value has the same binary representation as this `Long` value.
 */
inline fun Long.toULong() = ULong(this)

/**
 * Converts this [Float] value to [ULong].
 *
 * The fractional part, if any, is rounded down towards zero.
 * Returns zero if this `Float` value is negative or `NaN`, [ULong.MAX_VALUE] if it's bigger than `ULong.MAX_VALUE`.
 */
inline fun Float.toULong() = doubleToULong(toDouble())
/**
 * Converts this [Double] value to [ULong].
 *
 * The fractional part, if any, is rounded down towards zero.
 * Returns zero if this `Double` value is negative or `NaN`, [ULong.MAX_VALUE] if it's bigger than `ULong.MAX_VALUE`.
 */
inline fun Double.toULong() = doubleToULong(this)