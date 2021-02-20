@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions.tree

import ru.inforion.lab403.common.extensions.tree.BackwardIterator.Companion.backward
import java.io.Serializable

/**
 * Calculates depth of [this] node in tree
 *
 * NOTE: We can't cache depth because if node changes parent or children it require to recalculate
 *   caches of whole hierarchy and then node linking will be very expensive operation
 */
inline fun <T: Serializable> Node<T>.depth() = backward.count() - 1

/**
 * Creates new node with specified [content] and add it to the this node
 *
 * @param content is a content set into new node
 */
inline fun <T: Serializable> Node<T>.create(content: T) = Node(content).also { add(it) }

/**
 * Returns list of children (not iterator) to safe remove it all
 */
inline fun <T: Serializable> Node<T>.listOfChildren() = map { it }

/**
 * Removes and unlinks all children of [this] node
 */
inline fun <T: Serializable> Node<T>.clear() = listOfChildren().forEach { remove(it) }

/**
 * Adds all [newChildren] to [this] node
 */
inline fun <T: Serializable> Node<T>.addAll(newChildren: List<Node<T>>) = newChildren.forEach { add(it) }