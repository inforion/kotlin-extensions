@file:Suppress("NOTHING_TO_INLINE", "EXPERIMENTAL_FEATURE_WARNING")

package ru.inforion.lab403.common.extensions.tree

import ru.inforion.lab403.common.extensions.sure
import java.io.Serializable

/**
 * Tracks nodes by specified [predicate]
 * NOTE: Iterator checks nodes until first unmatched found starting from [target] node
 *   i.e. if [target] node matched then all children will be checked looking for first match and if found
 *   function goes to the next depth level and so on
 *
 * For the tree shown below result of:
 * ```
 * - track is [root, child0, child01, child010]
 * - filter is [root, child0, child001, child01, child010, child011]
 * - find is [root, ]
 *                                    |- child000
 *                                    |
 *                                    |
 *                        |- child00 -|
 *                        |           |
 *                        |           | [matched]
 *              [matched] |           |- child001
 *            |- child0 --|
 *            |           |             [matched]
 *            |           |           |- child010
 *            |           | [matched] |
 *            |           |- child01 -|
 *            |                       | [matched]
 * [matched]  |                       |- child011
 *    root ---|
 *            |           |-
 *            |- child1 --|
 *                        |-
 *
 * ```
 *
 * @param target node
 * @param predicate function to track nodes
 *
 */
class TrackIterator<T : Serializable>(val target: Node<T>, val predicate: (Node<T>) -> Boolean) : Iterable<Node<T>>{

    companion object {
        fun <T : Serializable> Node<T>.track(predicate: (Node<T>) -> Boolean) = TrackIterator(this, predicate)
    }

    override fun iterator() = object : Iterator<Node<T>> {
        private var next: Node<T>? = target

        override fun hasNext() = next != null

        override fun next(): Node<T> {
            val current = next.sure { "Tree has no next element" }

            next = current.children.firstOrNull { predicate(it) }

            return current
        }
    }
}