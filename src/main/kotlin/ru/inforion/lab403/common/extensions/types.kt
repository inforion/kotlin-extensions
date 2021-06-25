@file:Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions

inline fun <T> Any?.cast(): T = this as T
