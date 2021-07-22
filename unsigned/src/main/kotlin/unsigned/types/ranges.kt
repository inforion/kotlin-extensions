@file:Suppress("NOTHING_TO_INLINE", "unused")

package unsigned.types

import unsigned.literal.*
import unsigned.ranges.*

@PublishedApi
internal fun checkStepIsPositive(isPositive: Boolean, step: Number) {
    if (!isPositive) throw IllegalArgumentException("Step must be positive, was: $step.")
}

/**
 * Returns `true` if this range contains the specified [element].
 *
 * Always returns `false` if the [element] is `null`.
 */
inline operator fun UIntRange.contains(element: UInt?) = element != null && contains(element)

/**
 * Returns `true` if this range contains the specified [element].
 *
 * Always returns `false` if the [element] is `null`.
 */
inline operator fun ULongRange.contains(element: ULong?) = element != null && contains(element)

/**
 * Checks if the specified [value] belongs to this range.
 */
inline operator fun UIntRange.contains(value: UByte) = contains(value.toUInt())

/**
 * Checks if the specified [value] belongs to this range.
 */
inline operator fun ULongRange.contains(value: UByte) = contains(value.toULong())

/**
 * Checks if the specified [value] belongs to this range.
 */
inline operator fun ULongRange.contains(value: UInt) = contains(value.toULong())

/**
 * Checks if the specified [value] belongs to this range.
 */
inline operator fun UIntRange.contains(value: ULong) = (value shr UInt.SIZE_BITS) == Ol && contains(value.toUInt())

/**
 * Checks if the specified [value] belongs to this range.
 */
inline operator fun UIntRange.contains(value: UShort) = contains(value.toUInt())

/**
 * Checks if the specified [value] belongs to this range.
 */
inline operator fun ULongRange.contains(value: UShort) = contains(value.toULong())

/**
 * Returns a progression from this value down to the specified [to] value with the step -1.
 *
 * The [to] value should be less than or equal to `this` value.
 * If the [to] value is greater than `this` value the returned progression is empty.
 */
inline infix fun UByte.downTo(to: UByte) = UIntProgression.fromClosedRange(toUInt(), to.toUInt(), -1)

/**
 * Returns a progression from this value down to the specified [to] value with the step -1.
 *
 * The [to] value should be less than or equal to `this` value.
 * If the [to] value is greater than `this` value the returned progression is empty.
 */
inline infix fun UInt.downTo(to: UInt) = UIntProgression.fromClosedRange(this, to, -1)

/**
 * Returns a progression from this value down to the specified [to] value with the step -1.
 *
 * The [to] value should be less than or equal to `this` value.
 * If the [to] value is greater than `this` value the returned progression is empty.
 */
inline infix fun ULong.downTo(to: ULong) = ULongProgression.fromClosedRange(this, to, -1L)

/**
 * Returns a progression from this value down to the specified [to] value with the step -1.
 *
 * The [to] value should be less than or equal to `this` value.
 * If the [to] value is greater than `this` value the returned progression is empty.
 */
inline infix fun UShort.downTo(to: UShort) = UIntProgression.fromClosedRange(toUInt(), to.toUInt(), -1)

/**
 * Returns a progression that goes over the same range in the opposite direction with the same step.
 */
inline fun UIntProgression.reversed() = UIntProgression.fromClosedRange(last, first, -step)

/**
 * Returns a progression that goes over the same range in the opposite direction with the same step.
 */
inline fun ULongProgression.reversed() = ULongProgression.fromClosedRange(last, first, -step)

/**
 * Returns a progression that goes over the same range with the given step.
 */
inline infix fun UIntProgression.step(step: Int): UIntProgression {
    checkStepIsPositive(step > 0, step)
    return UIntProgression.fromClosedRange(first, last, if (this.step > 0) step else -step)
}

/**
 * Returns a progression that goes over the same range with the given step.
 */
inline infix fun ULongProgression.step(step: Long): ULongProgression {
    checkStepIsPositive(step > 0, step)
    return ULongProgression.fromClosedRange(first, last, if (this.step > 0) step else -step)
}

/**
 * Returns a range from this value up to but excluding the specified [to] value.
 *
 * If the [to] value is less than or equal to `this` value, then the returned range is empty.
 */
inline infix fun UByte.until(to: UByte): UIntRange {
    if (to <= UByte.MIN_VALUE) return UIntRange.EMPTY
    return toUInt() .. (to - I).toUInt()
}

/**
 * Returns a range from this value up to but excluding the specified [to] value.
 *
 * If the [to] value is less than or equal to `this` value, then the returned range is empty.
 */
inline infix fun UInt.until(to: UInt): UIntRange {
    if (to <= UInt.MIN_VALUE) return UIntRange.EMPTY
    return this .. (to - I).toUInt()
}

/**
 * Returns a range from this value up to but excluding the specified [to] value.
 *
 * If the [to] value is less than or equal to `this` value, then the returned range is empty.
 */
inline infix fun ULong.until(to: ULong): ULongRange {
    if (to <= ULong.MIN_VALUE) return ULongRange.EMPTY
    return this .. (to - I).toULong()
}

/**
 * Returns a range from this value up to but excluding the specified [to] value.
 *
 * If the [to] value is less than or equal to `this` value, then the returned range is empty.
 */
inline infix fun UShort.until(to: UShort): UIntRange {
    if (to <= UShort.MIN_VALUE) return UIntRange.EMPTY
    return toUInt() .. (to - I).toUInt()
}

/**
 * Ensures that this value is not less than the specified [minimumValue].
 *
 * @return this value if it's greater than or equal to the [minimumValue] or the [minimumValue] otherwise.
 */
inline fun UInt.coerceAtLeast(minimumValue: UInt) = if (this < minimumValue) minimumValue else this

/**
 * Ensures that this value is not less than the specified [minimumValue].
 *
 * @return this value if it's greater than or equal to the [minimumValue] or the [minimumValue] otherwise.
 */
inline fun ULong.coerceAtLeast(minimumValue: ULong) = if (this < minimumValue) minimumValue else this

/**
 * Ensures that this value is not less than the specified [minimumValue].
 *
 * @return this value if it's greater than or equal to the [minimumValue] or the [minimumValue] otherwise.
 */
inline fun UByte.coerceAtLeast(minimumValue: UByte) = if (this < minimumValue) minimumValue else this

/**
 * Ensures that this value is not less than the specified [minimumValue].
 *
 * @return this value if it's greater than or equal to the [minimumValue] or the [minimumValue] otherwise.
 */
inline fun UShort.coerceAtLeast(minimumValue: UShort) = if (this < minimumValue) minimumValue else this

/**
 * Ensures that this value is not greater than the specified [maximumValue].
 *
 * @return this value if it's less than or equal to the [maximumValue] or the [maximumValue] otherwise.
 */
inline fun UInt.coerceAtMost(maximumValue: UInt) = if (this > maximumValue) maximumValue else this

/**
 * Ensures that this value is not greater than the specified [maximumValue].
 *
 * @return this value if it's less than or equal to the [maximumValue] or the [maximumValue] otherwise.
 */
inline fun ULong.coerceAtMost(maximumValue: ULong) = if (this > maximumValue) maximumValue else this

/**
 * Ensures that this value is not greater than the specified [maximumValue].
 *
 * @return this value if it's less than or equal to the [maximumValue] or the [maximumValue] otherwise.
 */
inline fun UByte.coerceAtMost(maximumValue: UByte) = if (this > maximumValue) maximumValue else this

/**
 * Ensures that this value is not greater than the specified [maximumValue].
 *
 * @return this value if it's less than or equal to the [maximumValue] or the [maximumValue] otherwise.
 */
inline fun UShort.coerceAtMost(maximumValue: UShort) = if (this > maximumValue) maximumValue else this

/**
 * Ensures that this value lies in the specified range [minimumValue]..[maximumValue].
 *
 * @return this value if it's in the range, or [minimumValue] if this value is less than [minimumValue], or [maximumValue] if this value is greater than [maximumValue].
 */
inline fun UInt.coerceIn(minimumValue: UInt, maximumValue: UInt): UInt {
    if (minimumValue > maximumValue) throw IllegalArgumentException("Cannot coerce value to an empty range: maximum $maximumValue is less than minimum $minimumValue.")
    if (this < minimumValue) return minimumValue
    if (this > maximumValue) return maximumValue
    return this
}

/**
 * Ensures that this value lies in the specified range [minimumValue]..[maximumValue].
 *
 * @return this value if it's in the range, or [minimumValue] if this value is less than [minimumValue], or [maximumValue] if this value is greater than [maximumValue].
 */
inline fun ULong.coerceIn(minimumValue: ULong, maximumValue: ULong): ULong {
    if (minimumValue > maximumValue) throw IllegalArgumentException("Cannot coerce value to an empty range: maximum $maximumValue is less than minimum $minimumValue.")
    if (this < minimumValue) return minimumValue
    if (this > maximumValue) return maximumValue
    return this
}

/**
 * Ensures that this value lies in the specified range [minimumValue]..[maximumValue].
 *
 * @return this value if it's in the range, or [minimumValue] if this value is less than [minimumValue], or [maximumValue] if this value is greater than [maximumValue].
 */
inline fun UByte.coerceIn(minimumValue: UByte, maximumValue: UByte): UByte {
    if (minimumValue > maximumValue) throw IllegalArgumentException("Cannot coerce value to an empty range: maximum $maximumValue is less than minimum $minimumValue.")
    if (this < minimumValue) return minimumValue
    if (this > maximumValue) return maximumValue
    return this
}

/**
 * Ensures that this value lies in the specified range [minimumValue]..[maximumValue].
 *
 * @return this value if it's in the range, or [minimumValue] if this value is less than [minimumValue], or [maximumValue] if this value is greater than [maximumValue].
 */
inline fun UShort.coerceIn(minimumValue: UShort, maximumValue: UShort): UShort {
    if (minimumValue > maximumValue) throw IllegalArgumentException("Cannot coerce value to an empty range: maximum $maximumValue is less than minimum $minimumValue.")
    if (this < minimumValue) return minimumValue
    if (this > maximumValue) return maximumValue
    return this
}

/**
 * Ensures that this value lies in the specified [range].
 *
 * @return this value if it's in the [range], or `range.start` if this value is less than `range.start`, or `range.endInclusive` if this value is greater than `range.endInclusive`.
 */
inline fun UInt.coerceIn(range: ClosedRange<UInt>): UInt {
    if (range is ClosedFloatingPointRange) {
        return this.coerceIn<UInt>(range)
    }
    if (range.isEmpty()) throw IllegalArgumentException("Cannot coerce value to an empty range: $range.")
    return when {
        this < range.start -> range.start
        this > range.endInclusive -> range.endInclusive
        else -> this
    }
}

/**
 * Ensures that this value lies in the specified [range].
 *
 * @return this value if it's in the [range], or `range.start` if this value is less than `range.start`, or `range.endInclusive` if this value is greater than `range.endInclusive`.
 */
inline fun ULong.coerceIn(range: ClosedRange<ULong>): ULong {
    if (range is ClosedFloatingPointRange) {
        return this.coerceIn<ULong>(range)
    }
    if (range.isEmpty()) throw IllegalArgumentException("Cannot coerce value to an empty range: $range.")
    return when {
        this < range.start -> range.start
        this > range.endInclusive -> range.endInclusive
        else -> this
    }
}

