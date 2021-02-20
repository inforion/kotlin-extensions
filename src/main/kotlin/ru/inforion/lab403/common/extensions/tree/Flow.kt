package ru.inforion.lab403.common.extensions.tree

import ru.inforion.lab403.common.extensions.tree.DepthFirstIterator.Companion.dfs
import java.io.Serializable
import java.lang.IllegalStateException

class Flow<T : Serializable>(val nodes: List<Node<T>>, val items: List<Item<T>>) {
    data class Item<T : Serializable>(val source: Node<T>, val target: Node<T>)

    companion object {
        fun <T : Serializable> fromNode(node: Node<T>): Flow<T> {
            val nodes = node.dfs.map { it }

            val items = nodes.flatMap { parent ->
                parent.children.map { child -> Item(parent, child) }
            }

            return Flow(nodes, items)
        }

        fun <T : Serializable> Node<T>.toFlow() = fromNode(this)
    }

    fun interface Transformer<T, R> {
        fun call(value: T): R
    }

    private var valueCallback: Transformer<Node<T>, Any>? = null
    private var labelCallback: Transformer<Node<T>, String>? = null

    fun value(transform: Transformer<Node<T>, Any>) = apply { valueCallback = transform }
    fun label(transform: Transformer<Node<T>, String>) = apply { labelCallback = transform }

    fun toSankey(): Map<String, Any> {
        val localValueCallback = valueCallback ?: throw IllegalStateException("Value callback must be set!")
        val localLabelCallback = labelCallback

        val result = mutableMapOf<String, Any>()

        val indexed = nodes.mapIndexed { index, node -> node to index }.toMap()
        val values = nodes.map { localValueCallback.call(it) }

        val sources = items.map { indexed.getValue(it.source) }
        val targets = items.map { indexed.getValue(it.target) }

        result["sources"] = sources
        result["targets"] = targets
        result["values"] = targets.map { values[it] }

        if (localLabelCallback != null) result["labels"] = nodes.map { localLabelCallback.call(it) }

        return result
    }
}