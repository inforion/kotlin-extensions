@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package ru.inforion.lab403.common.extensions.tree

import java.io.Serializable
import java.io.StringWriter
import java.io.Writer

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
inline class DepthFirstRecursive<T: Serializable>(val target: Node<T>) {

    companion object {
        inline val <T : Serializable> Node<T>.dfs get() = DepthFirstRecursive(this)
    }

    /**
     * Filters all nodes by specified [predicate] into [result] list with indexing tree depth level starting from [current]
     * NOTE: Function checks **all** nodes: [target] node and all hierarchy either or not it met predicate condition
     *
     * @param result list of filter results
     * @param current start depth index (NOTE: not a starting depth)
     * @param predicate function to filter nodes into [result] list
     *
     * @see [trackWithDepthTo]
     *
     * @return [result] list
     */
    fun filterWithDepthTo(
        result: MutableList<Node<T>>,
        current: Int = 0,
        predicate: (Int, Node<T>) -> Boolean
    ): List<Node<T>> {
        val depth = current + 1
        if (predicate(current, target)) result.add(target)
        target.children.forEach { it.dfs.filterWithDepthTo(result, depth, predicate) }
        return result
    }

    /**
     * Filters all nodes by specified [predicate] with indexing tree depth level starting from [current]
     * NOTE: Function checks **all** nodes: [target] node and all hierarchy either or not it meet predicate condition
     *
     * @param current start depth index (NOTE: not a starting depth)
     * @param predicate function to filter nodes
     *
     * @see [trackWithDepthTo]
     *
     * @return list of filtered node that met predicate
     */
    fun filterWithDepth(current: Int = 0, predicate: (Int, Node<T>) -> Boolean) =
        filterWithDepthTo(mutableListOf(), current, predicate)

    /**
     * Filters all nodes by specified [predicate]
     * NOTE: Function checks **all** nodes: [target] node and all hierarchy either or not it meet predicate condition
     *
     * @param predicate function to filter nodes
     *
     * @see [trackWithDepthTo]
     *
     * @return list of filtered node that met predicate
     */
    fun filter(predicate: (Node<T>) -> Boolean) = filterWithDepthTo(mutableListOf(), 0) { _, node -> predicate(node) }

    /**
     * Finds first node by specified [predicate] with indexing tree depth level starting from [current]
     * NOTE: Function checks **all** nodes until node met [predicate] criteria found:
     *   [target] node and all hierarchy either or not it meet predicate condition
     *
     * @param current start depth index (NOTE: not a starting depth)
     * @param predicate function to find node
     *
     * @see [trackWithDepthTo]
     *
     * @return found node or null
     */
    fun findWithDepth(
        current: Int = 0,
        predicate: (Int, Node<T>) -> Boolean
    ): Node<T>? {
        val depth = current + 1
        if (predicate(current, target)) return target
        target.children.forEach {
            val result = it.dfs.findWithDepth(depth, predicate)
            if (result != null) return result
        }
        return null
    }

    /**
     * Finds first node by specified [predicate]
     * NOTE: Function checks **all** nodes until node met [predicate] criteria found:
     *   [target] node and all hierarchy either or not it meet predicate condition
     *
     * @param predicate function to find node
     *
     * @see [trackWithDepthTo]
     *
     * @return found node or null
     */
    fun find(predicate: (Node<T>) -> Boolean) = findWithDepth { _, node -> predicate(node) }

    /**
     * Tracks nodes by specified [predicate] into [result] list with indexing tree depth level starting from [current]
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
     * @param current start depth index (NOTE: not a starting depth)
     * @param predicate function to track nodes into [result] list
     *
     * @return [result] list of nodes track
     */
    fun trackWithDepthTo(
        result: MutableList<Node<T>>,
        current: Int = 0,
        predicate: (Int, Node<T>) -> Boolean
    ): MutableList<Node<T>> {
        if (predicate(current, target)) {
            result.add(target)
            val depth = current + 1
            target.children.forEach { it.dfs.trackWithDepthTo(result, depth, predicate) }
        }
        return result
    }

    /**
     * Tracks nodes by specified [predicate] with indexing tree depth level starting from [current]
     * NOTE: Function checks nodes until first unmatched found starting from [target] node
     *   i.e. if [target] node matched then all children will be checked looking for first match and if found
     *   function goes to the next depth level and so on
     *
     * @param current start depth index (NOTE: not a starting depth)
     * @param predicate function to track nodes
     *
     * @see [trackWithDepthTo]
     */
    fun trackWithDepth(current: Int = 0, predicate: (Int, Node<T>) -> Boolean) =
        trackWithDepthTo(mutableListOf(), current, predicate)

    /**
     * Tracks nodes by specified [predicate]
     * NOTE: Function checks nodes until first unmatched found starting from [target] node
     *   i.e. if [target] node matched then all children will be checked looking for first match and if found
     *   function goes to the next depth level and so on
     *
     * @param predicate function to track nodes
     *
     * @see [trackWithDepthTo]
     */
    fun track(predicate: (Node<T>) -> Boolean) = trackWithDepth { _, node -> predicate(node) }

    /**
     * Executes given [action] for all nodes with indexing tree depth level starting from [current]
     * NOTE: Function executed for **all** nodes: [target] node and all hierarchy
     *
     * @param current start depth index (NOTE: not a starting depth)
     * @param action function to execute for all nodes
     */
    fun forEachWithDepth(current: Int = 0, action: (Int, Node<T>) -> Unit) {
        val depth = current + 1
        action(current, target)
        target.children.forEach { it.dfs.forEachWithDepth(depth, action) }
    }

    /**
     * Executes given [action] for all nodes
     * NOTE: Function executed for **all** nodes: [target] node and all hierarchy
     *
     * @param action function to execute for all nodes
     */
    fun forEach(action: (Node<T>) -> Unit) = forEachWithDepth { _, node -> action(node) }

    /**
     * Maps tree started with [target] node using [transform] function into [result] plain list
     *   with indexing tree depth level starting from [current]
     * NOTE: Function executed for **all** nodes: [target] node and all hierarchy
     *
     * @param result output transformed plain list
     * @param current start depth index (NOTE: not a starting depth)
     * @param transform function to transform each node
     */
    fun <R> mapWithDepthTo(result: MutableList<R>, current: Int = 0, transform: (Int, Node<T>) -> R): MutableList<R> {
        val depth = current + 1
        result.add(transform(current, target))
        target.children.forEach { it.dfs.mapWithDepthTo(result, depth, transform) }
        return result
    }

    /**
     * Maps tree started with [target] node using [transform] function into plain list
     *   with indexing tree depth level starting from [current]
     * NOTE: Function executed for **all** nodes: [target] node and all hierarchy
     *
     * @param current start depth index (NOTE: not a starting depth)
     * @param transform function to transform each node
     */
    fun <R> mapWithDepth(current: Int = 0, transform: (Int, Node<T>) -> R) =
        mapWithDepthTo(mutableListOf(), current, transform)

    /**
     * Maps tree started with [target] node using [transform] function into plain list
     * NOTE: Function executed for **all** nodes: [target] node and all hierarchy
     *
     * @param transform function to transform each node
     */
    fun <R> map(transform: (Node<T>) -> R) = mapWithDepth { _, node -> transform(node) }

    /**
     * Writes tree into string in depth first traverse order
     *
     * @param writer where to write
     * @param ident is a ident size for each tree depth level
     * @param transform function to transform node to string
     *
     * @return writer
     */
    fun write(writer: Writer = StringWriter(), ident: Int = 4, transform: (Node<T>) -> String) = writer.apply {
        forEachWithDepth { depth, node ->
            repeat(ident * depth) { append(" ") }
            appendLine(transform(node))
        }
        flush()
    }

    /**
     * Transforms tree with [target] starting node into string in depth first traverse order
     *
     * @param ident is a ident size for each tree depth level
     * @param transform function to transform node to string
     *
     * @return result string
     */
    fun joinToString(ident: Int = 4, transform: (Node<T>) -> String) = write(ident = ident, transform = transform).toString()
}