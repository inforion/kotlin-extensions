@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.utils

inline fun <R, T>lazyTransient(noinline initializer: () -> T) = LazyTransient<R, T>(initializer)

inline fun <E> gettableHashSetOf(vararg values: E) = GettableHashSet<E>().apply { addAll(values) }

inline fun <E> GettableHashSet<E>.getValue(key: Any): E = get(key)
    ?: throw NoSuchElementException("Key $key is missing in the set.")