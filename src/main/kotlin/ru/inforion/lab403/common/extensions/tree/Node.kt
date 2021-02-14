package ru.inforion.lab403.common.extensions.tree

import ru.inforion.lab403.common.extensions.identity
import ru.inforion.lab403.common.extensions.lhex8
import java.io.Serializable

class Node<T: Serializable> constructor(val content: T) : Iterable<Node<T>>, Serializable {

    private val childrenIntern = mutableListOf<Node<T>>()

    /**
     * Nodes parent or null if node has no parent
     */
    var parent: Node<T>? = null
        private set (value) {
            field = value
            depth = if (value != null) value.depth + 1 else 0
        }

    /**
     * Returns depth of this node or recalculate it
     */
    var depth = 0
        private set

    /**
     * Returns immutable variant of node's children
     */
    val children: List<Node<T>> get() = childrenIntern

    /**
     * Removes specified [node] from children of this node
     *
     * @param node is a node to remove from children
     */
    fun remove(node: Node<T>): Boolean {
        if (childrenIntern.remove(node)) {
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
        childrenIntern.add(node)
    }

    /**
     * Creates new node with specified [content] and add it to the this node
     *
     * @param content is a content set into new node
     */
    fun create(content: T) = Node(content).also { add(it) }

    /**
     * Checks whether or not this node is root node
     */
    inline val isRoot get() = parent == null

    /**
     * Checks whether or not this node has children
     */
    val hasChildren get() = childrenIntern.isNotEmpty()

    /**
     * Iterates only over this node children with hierarchy
     */
    override fun iterator() = childrenIntern.iterator()

    /**
     * Returns nodes child with specified [index]
     *
     * @param index is a child index
     */
    operator fun get(index: Int) = childrenIntern[index]

    override fun toString() = "Node(id=${identity.lhex8})"
}