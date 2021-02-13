package ru.inforion.lab403.common.extensions.tree

import ru.inforion.lab403.common.extensions.identity
import ru.inforion.lab403.common.extensions.lhex8
import java.io.Serializable

class Node<T: Serializable> constructor(val content: T) : Serializable {
    internal var parent: Node<T>? = null
    internal val children = mutableListOf<Node<T>>()

    val isRoot get() = parent == null

    val hasChildren get() = children.isNotEmpty()

    fun removeChild(node: Node<T>): Boolean {
        if (children.remove(node)) {
            node.parent = null
            return true
        }
        return false
    }

    fun addChild(node: Node<T>) {
        node.unlink()
        node.parent = this
        children.add(node)
    }

    fun unlink() = parent?.removeChild(this) ?: false

    override fun toString() = "Node(id=${identity.lhex8})"
}