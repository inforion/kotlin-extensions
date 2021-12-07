@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.intervalmap

import ru.inforion.lab403.common.extensions.hex
import java.util.*


internal inline fun <T> MutableList<T>.copy() = toMutableList()

internal inline fun TreeMap<Mark, Ranges>.at(key: Mark) =
    get(key) ?: error("No mapping boundary at: 0x${key.hex}")

internal inline fun TreeMap<Mark, Ranges>.left(key: Mark): Ranges {
    val entry = floorEntry(key) ?: error("Access to uninitialized mapping area: 0x${key.hex}")
    return entry.value
}

internal inline fun TreeMap<Mark, Ranges>.right(key: Mark): Ranges {
    val entry = higherEntry(key) ?: error("Access to uninitialized mapping area: 0x${key.hex}")
    return entry.value
}

internal inline fun TreeMap<Mark, Ranges>.last(): Ranges = lastEntry().value

internal inline fun TreeMap<Mark, Ranges>.between(first: Mark, last: Mark) =
    keys.filter { first <= it && it < last }.sorted()


internal inline fun TreeMap<Mark, Ranges>.from(first: Mark) =
    keys.filter { first <= it }.sorted()
