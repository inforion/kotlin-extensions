@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.extensions

typealias Dictionary<K, V> = HashMap<K, V>

const val LOAD_FACTOR = 0.60f
const val DEFAULT_CAPACITY = 16

/**
 * Calculate the initial capacity of a map, based on Guava's
 * [com.google.common.collect.Maps.capacity](https://github.com/google/guava/blob/v28.2/guava/src/com/google/common/collect/Maps.java#L325)
 * approach.
 */
inline fun capacity(expectedSize: Int): Int = when {
    // We are not coercing the value to a valid one and not throwing an exception. It is up to the caller to
    // properly handle negative values.
    expectedSize < 0 -> expectedSize
    expectedSize < 3 -> expectedSize + 1
    expectedSize < INT_MAX_POWER_OF_TWO -> ((expectedSize / 0.75F) + 1.0F).toInt()
    // any large value
    else -> INT_MAX
}

const val INT_MAX_POWER_OF_TWO: Int = 1 shl (INT_BITS - 2)

inline fun <K, V> dictionary(capacity: Int = DEFAULT_CAPACITY, factor: Float = LOAD_FACTOR) =
    Dictionary<K, V>(capacity, factor)

inline fun <K, V> dictionaryOf(vararg pairs: Pair<K, V>) =
    dictionary<K, V>(capacity(pairs.size)).apply { putAll(pairs) }

inline fun <K, V> Dictionary<out K, V>.filter(predicate: (Map.Entry<K, V>) -> Boolean) = filterTo(dictionary(), predicate)

inline fun <V> List<V>.toDictionaryByIndex() = dictionaryOf<Int, V>().also {
    forEachIndexed { index, item -> it[index] = item }
}

inline fun <K, V> Iterable<K>.toDictionaryWith(valueSelector: (K) -> V) = associateWithTo(dictionary(), valueSelector)

inline fun <K, V> Map<K, V>.toDictionary() = dictionary<K, V>().also { it.putAll(this) }

inline fun <IK, IV, OK, OV> Map<IK, IV>.toDictionary(transform: (Map.Entry<IK, IV>) -> Pair<OK, OV>) =
    entries.associateTo(dictionary(), transform)

inline fun <K, V> Dictionary<K, V>.addAll(other: Dictionary<K, V>): Boolean {
    val total = keys.size
    putAll(other)
    return keys.size > total
}

inline fun <K, V> Dictionary<K, V>.removeIf(predicate: (Map.Entry<K, V>) -> Boolean): Boolean {
    var removed = false
    val each  = entries.iterator()
    while (each.hasNext()) {
        if (predicate(each.next())) {
            each.remove()
            removed = true
        }
    }
    return removed
}