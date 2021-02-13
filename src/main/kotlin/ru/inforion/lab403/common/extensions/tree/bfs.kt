package ru.inforion.lab403.common.extensions.tree

import java.io.Serializable
import java.io.StringWriter
import java.io.Writer

fun <T: Serializable> Node<T>.filterBreadthFirstWithDepthTo(
    result: MutableList<Node<T>>,
    current: Int = 0,
    predicate: (Int, Node<T>) -> Boolean
): List<Node<T>> {
    val depth = current + 1
    if (predicate(current, this)) result.add(this)
    children.forEach { it.findBreadthFirstWithDepth(depth, predicate) }
    return result
}

fun <T: Serializable> Node<T>.filterBreadthFirstWithDepth(current: Int = 0, predicate: (Int, Node<T>) -> Boolean) =
        filterBreadthFirstWithDepthTo(mutableListOf(), current, predicate)

fun <T: Serializable> Node<T>.filterBreadthFirst(predicate: (Node<T>) -> Boolean) =
        filterBreadthFirstWithDepthTo(mutableListOf(), 0) { _, node -> predicate(node) }

fun <T: Serializable> Node<T>.findBreadthFirstWithDepth(current: Int = 0, predicate: (Int, Node<T>) -> Boolean): Node<T>? {
    val depth = current + 1
    if (predicate(current, this)) return this
    children.forEach {
        val result = it.findBreadthFirstWithDepth(depth, predicate)
        if (result != null) return result
    }
    return null
}

fun <T: Serializable> Node<T>.findBreadthFirst(predicate: (Node<T>) -> Boolean) = findBreadthFirstWithDepth { _, node -> predicate(node) }

fun <T: Serializable> Node<T>.breadFirstWithDepth(current: Int = 0, action: (Int, Node<T>) -> Unit) {
    val depth = current + 1
    action(current, this)
    children.forEach { it.breadFirstWithDepth(depth, action) }
}

fun <T: Serializable> Node<T>.writeBreadFirst(ident: Int = 4, writer: Writer = StringWriter(), transform: (Node<T>) -> String) = writer.apply {
    breadFirstWithDepth { depth, node ->
        repeat(ident * depth) { append(" ") }
        appendLine(transform(node))
    }
    flush()
}

fun <T: Serializable> Node<T>.joinToStringBreadFirst(ident: Int = 4, transform: (Node<T>) -> String) =
        writeBreadFirst(ident, transform = transform).toString()

fun <T: Serializable> Node<T>.breadFirst(action: (Node<T>) -> Unit) = breadFirstWithDepth { _, node -> action(node) }