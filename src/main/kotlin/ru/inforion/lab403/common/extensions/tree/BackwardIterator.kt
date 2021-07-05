@file:Suppress("NOTHING_TO_INLINE", "EXPERIMENTAL_FEATURE_WARNING")

package ru.inforion.lab403.common.extensions.tree

import ru.inforion.lab403.common.extensions.sure
import java.io.Serializable

@JvmInline
value class BackwardIterator<T : Serializable>(val target: Node<T>) : Iterable<Node<T>> {
    companion object {
        inline val <T : Serializable> Node<T>.backward get() = BackwardIterator(this)
    }

    override fun iterator() = object : Iterator<Node<T>> {
        private var next: Node<T>? = target

        override fun hasNext() = next != null

        override fun next(): Node<T> {
            val current = next.sure { "Tree has no next element" }
            return current.also { next = it.parent }
        }
    }
}