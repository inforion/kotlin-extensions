package ru.inforion.lab403.common.extensions.tree

import ru.inforion.lab403.common.extensions.identity
import ru.inforion.lab403.common.extensions.lhex8
import java.io.Serializable

class Node<T: Serializable> constructor(val content: T) : Iterable<Node<T>>, Serializable {

    private val myChildren = mutableListOf<Node<T>>()

    /**
     * Nodes parent or null if node has no parent
     */
    var parent: Node<T>? = null
        private set

    /**
     * Returns immutable variant of node's children
     */
    val children: List<Node<T>> get() = myChildren

    /**
     * Removes specified [node] from children of this node
     *
     * @param node is a node to remove from children
     */
    fun remove(node: Node<T>): Boolean {
        if (myChildren.remove(node)) {
            node.parent = null
            return true
        }
        return false
    }

    /**
     * Removes this node from its parent but left children intact
     */
    fun unlink() = parent?.remove(this) ?: false

    /**
     * Adds specified [node] to this node
     *
     * @param node is a node to add to current
     */
    fun add(node: Node<T>) {
        node.unlink()
        node.parent = this
        myChildren.add(node)
    }

    /**
     * Checks whether or not this node is root node
     */
    inline val isRoot get() = parent == null

    /**
     * Checks whether or not this node has children
     */
    val hasChildren get() = myChildren.isNotEmpty()

    /**
     * Iterates only over this node children with hierarchy
     */
    override fun iterator() = myChildren.iterator()

    /**
     * Returns nodes child with specified [index]
     *
     * @param index is a child index
     */
    operator fun get(index: Int) = myChildren[index]

    override fun toString() = "Node@${identity.lhex8}"
}