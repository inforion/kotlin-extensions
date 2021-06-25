@file:Suppress("unused", "NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions

import java.security.SecureRandom
import java.util.concurrent.ThreadLocalRandom

// ThreadLocalRandom wrappers

val random: ThreadLocalRandom get() = ThreadLocalRandom.current()

/**
 * Get random long with inclusive lower and exclusive upper boundary
 */
inline fun ThreadLocalRandom.long(origin: Long, bound: Long) = nextLong(origin, bound)

/**
 * Get random long with exclusive upper boundary
 */
inline fun ThreadLocalRandom.long(bound: Long) = nextLong(bound)

/**
 * Get random long
 */
inline val ThreadLocalRandom.long get() = nextLong()

// Random function simplifier for Integer

/**
 * Get random integer with inclusive lower and exclusive upper boundary
 */
inline fun ThreadLocalRandom.int(origin: Int, bound: Int) = nextInt(origin, bound)

/**
 * Get random integer with exclusive upper boundary
 */
inline fun ThreadLocalRandom.int(bound: Int) = nextInt(bound)

/**
 * Get random integer
 */
inline val ThreadLocalRandom.int get() = nextInt()

// Random function simplifier for Unsigned Integer

/**
 * Get random unsigned integer with inclusive lower and exclusive upper boundary
 */
inline fun ThreadLocalRandom.uint(origin: Long, bound: Long): Long = long(origin, bound) and INT32MASK

/**
 * Get random unsigned integer with exclusive upper boundary
 */
inline fun ThreadLocalRandom.uint(bound: Long): Long = uint(0, bound)

/**
 * Get random unsigned integer
 */
inline val ThreadLocalRandom.uint get(): Long = uint(maxUIntValue + 1)

// Random function simplifier for Short

/**
 * Get random short with inclusive lower and exclusive upper boundary
 */
inline fun ThreadLocalRandom.short(origin: Short, bound: Short) = int(origin.asUInt, bound.asUInt).asShort

/**
 * Get random short with exclusive upper boundary
 */
inline fun ThreadLocalRandom.short(bound: Short) = short(0, bound)

/**
 * Get random short
 */
inline val ThreadLocalRandom.short get() = int(minShortValue + 0, maxShortValue + 1).asShort

// Random function simplifier for Byte

/**
 * Get random byte with inclusive lower and exclusive upper boundary
 */
inline fun ThreadLocalRandom.byte(origin: Byte, bound: Byte) = int(origin.asUInt, bound.asUInt).asByte

/**
 * Get random byte with exclusive upper boundary
 */
inline fun ThreadLocalRandom.byte(bound: Byte) = byte(0, bound)

/**
 * Get random byte
 */
inline val ThreadLocalRandom.byte get() = int(minByteValue + 0, maxByteValue + 1).asByte

// Random function simplifier for Double

/**
 * Get random double with inclusive lower and exclusive upper boundary
 */
inline fun ThreadLocalRandom.double(origin: Double, bound: Double) = nextDouble(origin, bound)

/**
 * Get random double with exclusive upper boundary
 */
inline fun ThreadLocalRandom.double(bound: Double) = nextDouble(bound)

/**
 * Get random double between zero (inclusive) and one (exclusive).
 */
inline val ThreadLocalRandom.double get() = nextDouble()

// Random function simplifier for ByteArray

/**
 * Fill with random specified byte array
 */
inline fun ThreadLocalRandom.bytes(bytes: ByteArray) = nextBytes(bytes)

/**
 * Get random byte array
 */
inline fun ThreadLocalRandom.bytes(size: Int) = ByteArray(size).also { bytes(it) }

// Random function misc

/**
 * Make an unbalanced coin flip
 */
inline fun ThreadLocalRandom.success(rate: Double) = double < rate

// Random function simplifier for Collection

/**
 * Get random element from collections according weights function from index and item
 *
 * @param pdf probability density function
 */
inline fun <E>Collection<E>.randomIndexed(pdf: (Int, E) -> Double): E {
    if (this.size == 1)
        return first()
    var cum = 0.0
    val pairs = mapIndexed { i, e ->
        cum += pdf(i, e)
        Pair(e, cum)
    }
    return if (cum == 0.0) {
        val r = random.int(pairs.size)
        pairs[r].first
    } else {
        val r = random.double(cum)
        pairs.first { it.second >= r }.first
    }
}

/**
 * Get random element from collections according weights function from item
 *
 * @param pdf probability density function
 */
inline fun <E>Collection<E>.random(pdf: (E) -> Double) = randomIndexed { _, e -> pdf(e) }

/**
 * Get random element specified arguments
 */
fun <T> random(vararg items: T) = items.random()

// Random function deprecated

@Deprecated("use will be renamed to randomIndexed() instead")
inline fun <E>Collection<E>.choiceIndexed(block: (Int, E) -> Double) = randomIndexed(block)

@Deprecated("use will be renamed to random() instead")
fun <E>Collection<E>.choice(block: (E) -> Double) = random(block)

/**
 * Get random element from collections (uniform)
 */
@Deprecated("use Kotlin random() instead")
fun <E>Collection<E>.choice() = random()

/**
 * Get random element index according to weight at this index
 */
@Deprecated("use Kotlin random() instead")
fun IntArray.choice(): Int = random()