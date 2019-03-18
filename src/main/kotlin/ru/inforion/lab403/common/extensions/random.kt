@file:Suppress("unused")

package ru.inforion.lab403.common.extensions

//import org.kohsuke.randname.RandomNameGenerator
//import ru.inforion.lab403.common.specials.NameGenerator
import java.util.*
import java.util.concurrent.ThreadLocalRandom

/**
 * Thread-safe random generators wrappers
 */
object random {
//    private val stupidNameGenerator = RandomNameGenerator()
//    private val fantasyNameGenerator = NameGenerator()

    /**
     * Generate pseudo-random signed int value in range [ min..max ]
     * with lower boundary inclusive and upper exclusive
     */
    fun randint(range: IntRange = Int.MIN_VALUE..Int.MAX_VALUE): Int = ThreadLocalRandom.current().nextInt(range.first, range.last)
    fun randint(upper: Int): Int = ThreadLocalRandom.current().nextInt(upper)

    fun randbyte(): Byte = randint(0..256).toByte()

    /**
     * Generate pseudo-random unsigned int (as Long type) value in range [ min..max ]
     * with lower boundary inclusive and upper exclusive
     */
    fun randuint(range: LongRange = 0..0xFFFFFFFF): Long {
        val min = range.first and 0xFFFFFFFF
        val max = range.last and 0xFFFFFFFF
        val size = max - min
        val rnd = (ThreadLocalRandom.current().nextDouble() * size).toULong()
        return min + rnd
    }

    fun rand(bound: Double): Double {
        return ThreadLocalRandom.current().nextDouble(bound)
    }

    fun randbytes(data: ByteArray) {
        ThreadLocalRandom.current().nextBytes(data)
    }

    fun randbytes(size: Int): ByteArray {
        val result = ByteArray(size)
        randbytes(result)
        return result
    }

    fun success(prob: Double): Boolean {
        return ThreadLocalRandom.current().nextDouble() < prob
    }

    @kotlin.Deprecated("Use collection choice extension method")
    fun <E>choice(items: List<E>, prob: Array<Double>? = null): E {
        if (prob == null || prob.sum() == 0.0) {
            val r = randint(0..items.size)
            return items[r]
        }
        return items.choiceIndexed { i, _ -> prob[i] }
    }

//    fun stupidName(length: Int = -1): String {
//        var name: String
//        do {
//            val trail = stupidNameGenerator.next()
//            val (first, last) = trail.split("_")
//            name = "${first.capitalize()} ${last.capitalize()}"
//        } while (length != -1 && name.length >= length)
//        return name
//    }
//
//    fun fantasyName(length: Int = -1): String {
//        var name: String
//        do {
//            val first = fantasyNameGenerator.compose(randint(1..length / 4))
//            val last = fantasyNameGenerator.compose(randint(1..length / 4))
//            name = "$first $last"
//        } while (length != -1 && name.length >= length)
//        return name
//    }
}


@kotlin.Deprecated("Use collection choice extension method")
fun <T>ArrayList<T>.choice(weights: Array<Double>): T {
    assert(weights.size == this.size)
    val total = weights.sum()
    val cumWeights = Array(weights.size) { k -> weights.slice(0..k).sum() }
    val x = Math.random() * total
    val i = cumWeights.bisectLeft(x)
    return this[i]
}


/**
 * Get random element from collections according weights function from index and item.
 */
fun <E>Collection<E>.choiceIndexed(block: (Int, E) -> Double): E {
    if (this.size == 1)
        return this.first()
    var cum = 0.0
    val pairs = this.mapIndexed { i, e ->
        cum += block(i, e)
        Pair(e, cum)
    }
    if (cum == 0.0) {
        val r = random.randint(0..pairs.size)
        return pairs[r].first
    } else {
        val r = random.rand(cum)
        return pairs.first { it.second >= r }.first
    }
}

/**
 * Get random element from collections according weights function from item.
 */
fun <E>Collection<E>.choice(block: (E) -> Double): E = this.choiceIndexed { _, e -> block(e) }

/**
 * Get random element from collections (uniform)
 */
fun <E>Collection<E>.choice(): E = this.choice { 0.0 }

/**
 * Get random element index according to weight at this index
 */
fun IntArray.choice(): Int = this.indices.toList().choice { i -> this[i].toDouble() }
