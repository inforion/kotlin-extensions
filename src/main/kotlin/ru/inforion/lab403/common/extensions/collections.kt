@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions

inline fun <T>Iterable<T?>.forEachNotNull(block: (T) -> Unit) = forEach {
    if (it != null) block(it)
}

inline fun <T>Array<T?>.forEachNotNull(block: (T) -> Unit) = forEach {
    if (it != null) block(it)
}

@Deprecated("just use List(count) { ... } since 0.3.4")
inline fun <T> collect(count: Int, item: (Int) -> T) = (0 until count).map(item)

@Deprecated("just use range(...) since 0.3.4")
inline fun collect(count: Int) = (0 until count).toList()

/**
 * Returns sequence with total specified element [count] and [block] initializer for each
 *
 * @since 0.3.4
 */
inline fun <T> sequence(count: Int, crossinline block: (Int) -> T) = sequence { repeat(count) { yield(block(it)) } }

/**
 * Returns a single map of all elements from all maps in the given collection.
 *
 * @since 0.3.4
 */
inline fun <T, R>Collection<Map<T, R>>.flatten() = mutableMapOf<T, R>().also { map -> forEach { map += it } }