@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package ru.inforion.lab403.common.extensions.tree

import ru.inforion.lab403.common.extensions.sure
import java.io.Serializable
import java.io.StringWriter
import java.io.Writer
import java.util.*

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
@JvmInline
value class DepthFirstIterator<T: Serializable>(val target: Node<T>) : Iterable<Node<T>> {

    companion object {
        inline val <T : Serializable> Node<T>.dfs get() = DepthFirstIterator(this)
    }

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