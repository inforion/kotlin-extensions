@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.extensions

inline fun Throwable.stackTraceToStringOrNull() = if (stackTrace != null) stackTraceToString() else null