@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions

inline fun <T>Iterable<T?>.forEachNotNull(block: (T) -> Unit) = forEach {
    if (it != null) block(it)
}

inline fun <T>Array<T?>.forEachNotNull(block: (T) -> Unit) = forEach {
    if (it != null) block(it)
}

inline fun <T> collect(count: Int, item: (Int) -> T) = (0 until count).map(item)

inline fun collect(count: Int) = (0 until count).toList()