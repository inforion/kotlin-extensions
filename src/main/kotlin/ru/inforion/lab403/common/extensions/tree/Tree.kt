@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions.tree

import ru.inforion.lab403.common.extensions.tree.DepthFirstIterator.Companion.dfs
import java.io.Serializable

data class Tree<T>(
    val sources: List<Int>,
    val indexes: List<Int>,
    val contents: List<T>
) {
    companion object {
        inline fun <T : Serializable, R> fromRoot(node: Node<T>, transform: (Node<T>) -> R): Tree<R> {
            val nodes = node.dfs.map { it }

            val indexed = nodes.mapIndexed { index, it -> it to index }.toMap()

            val siblings = nodes.drop(1)

            val sources = siblings.map { indexed.getValue(it.parent!!) }
            val targets = siblings.map { indexed.getValue(it) }

            val values = nodes.map { transform(it) }

            return Tree(sources, targets, values)
        }

        inline fun <T : Serializable, R> Node<T>.toTree(transform: (Node<T>) -> R) = fromRoot(this, transform)

        inline fun <T : Serializable> Node<T>.toTreeMapValue(
            transform: MutableMap<String, Any>.(Node<T>) -> Unit
        ) = fromRoot(this) { mutableMapOf<String, Any>().apply { transform(it) } as Map<String, Any> }

        inline fun <T : Serializable> Node<T>.toTree() = fromRoot(this) { it.content }
    }
}