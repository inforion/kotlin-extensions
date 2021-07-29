@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.json

import kotlinx.serialization.json.JsonPrimitive

// JsonPrimitive extensions methods

inline fun JsonPrimitive.parse(): Comparable<*>? = when {
    isString -> content
    content == "null" -> null
    else -> with(content) {
        toIntOrNull(10) ?:
        toLongOrNull(10) ?:
        toUIntOrNull(10) ?:
        toULongOrNull(10) ?:
        toBooleanStrictOrNull() ?:
        toFloatOrNull() ?:
        toDoubleOrNull()
    }
}