package ru.inforion.lab403.common.utils

import ru.inforion.lab403.common.extensions.cast
import ru.inforion.lab403.common.extensions.int
import java.io.Serializable
import java.util.AbstractSet

class GettableHashSet<E> private constructor(
    private val map: HashMap<Any, E> = HashMap()
) : AbstractSet<E>(), MutableSet<E>, Serializable {

    companion object {
        private fun <E> map(size: Int): HashMap<Any, E> {
            val supposedCapacity = (size / .75f).int + 1
            val initialCapacity = supposedCapacity.coerceAtLeast(16)
            return HashMap(initialCapacity)
        }
    }

    constructor() : this(HashMap())

    constructor(c: Collection<E>) : this(map(c.size)) { addAll(c) }

    constructor(initialCapacity: Int, loadFactor: Float) : this(HashMap(initialCapacity, loadFactor))

    constructor(initialCapacity: Int) : this(HashMap(initialCapacity))

    override fun iterator(): MutableIterator<E> = map.keys.iterator().cast()

    override fun isEmpty() = map.isEmpty()

    override fun contains(element: E) = map.containsKey(element.cast())

    override fun add(element: E) = map.put(element.cast(), element) == null

    override fun remove(element: E): Boolean = map.remove(element.cast()) != null

    operator fun get(element: Any): E? = map[element]

    override val size get(): Int = map.size

    override fun clear(): Unit = map.clear()

    // Workflow, we can't call extension functions from python scripts
    fun take(n: Int) = (this as Set<E>).take(n)
}