@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.utils

inline fun <R, T>lazyTransient(noinline initializer: () -> T) = LazyTransient<R, T>(initializer)