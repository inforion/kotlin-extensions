@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions.tree

import java.io.Serializable
import kotlin.reflect.KProperty

inline fun <T : Serializable> Node<T>.calculateDepth(): Int {
    var result = 0
    var current = this
    while (current.parent != null) {
        current = current.parent!!
        result++
    }
    return result
}