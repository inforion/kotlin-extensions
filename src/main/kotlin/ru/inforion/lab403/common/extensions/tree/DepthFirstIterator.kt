@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package ru.inforion.lab403.common.extensions.tree

import ru.inforion.lab403.common.extensions.sure
import java.io.Serializable
import java.io.StringWriter
import java.io.Writer
import java.util.*

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
inline class DepthFirstIterator<T: Serializable>(val target: Node<T>) : Iterable<Node<T>> {

    companion object {
        inline val <T : Serializable> Node<T>.dfs get() = DepthFirstIterator(this)
    }

    /**
     * Tracks nodes by specified [predicate] into [result] list
     * NOTE: Function checks nodes until first unmatched found starting from [target] node
     *   i.e. if [target] node matched then all children will be checked looking for first match and if found
     *   function goes to the next depth level and so on
     *
     * For the tree shown below result of:
     * ```
     * - track is [root, child0, child01, child010, child011] NOTE: also child011
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
     * @param result list of tracked nodes
     * @param predicate function to track nodes into [result] list
     *
     * @return [result] list of nodes track
     */
    fun trackTo(result: MutableList<Node<T>>, predicate: (Node<T>) -> Boolean): MutableList<Node<T>> {
        if (predicate(target)) {
            result.add(target)
            target.children.forEach { it.dfs.trackTo(result, predicate) }
        }
        return result
    }

    /**
     * Tracks nodes by specified [predicate]
     * NOTE: Function checks nodes until first unmatched found starting from [target] node
     *   i.e. if [target] node matched then all children will be checked looking for first match and if found
     *   function goes to the next depth level and so on
     *
     * @param predicate function to track nodes
     *
     * @see [trackTo]
     */
    fun track(predicate: (Node<T>) -> Boolean) = trackTo(mutableListOf(), predicate)

    /**
     * Writes tree into string in depth first traverse order
     *
     * @param writer where to write
     * @param ident is a ident size for each tree depth level
     * @param separator is a char for ident
     * @param transform function to transform node to string
     *
     * @return writer
     */
    fun write(
        writer: Writer = StringWriter(),
        ident: Int = 4,
        separator: Char = ' ',
        transform: (Node<T>) -> String
    ) = writer.apply {
        forEach { node ->
            repeat(ident * node.depth()) { append(separator) }
            appendLine(transform(node))
        }
        flush()
    }

    /**
     * Prints tree into string in depth first traverse order
     *
     * @param ident is a ident size for each tree depth level
     * @param separator is a char for ident
     * @param transform function to transform node to string
     */
    fun print(ident: Int = 4, separator: Char = ' ', transform: (Node<T>) -> String) {
        with(System.out.writer()) {
            write(this, ident, separator, transform).toString()
            appendLine()
        }
    }

    /**
     * Converts tree into string in depth first traverse order
     *
     * @param ident is a ident size for each tree depth level
     * @param separator is a char for ident
     * @param transform function to transform node to string
     *
     * @return writer
     */
    fun toString(ident: Int = 4, separator: Char = ' ', transform: (Node<T>) -> String) =
        write(StringWriter(), ident, separator, transform).toString()

    override fun iterator() = object : Iterator<Node<T>> {
        private var next: Node<T>? = target

        private var index = 0
        private val stack = LinkedList<Pair<Int, Node<T>>>()

        override fun hasNext() = next != null

        override fun next(): Node<T> {
            val current = next.sure { "Tree has no next element" }

            var node: Node<T> = current

            if (index == current.children.size) {
                do {
                    if (stack.size == 0) {
                        next = null
                        return current
                    }
                    val pair = stack.pop()
                    index = pair.first
                    node = pair.second
                } while (index == node.children.size)
            }

            stack.push(index + 1 to node)
            next = node.children[index]
            index = 0

            return current
        }
    }
}