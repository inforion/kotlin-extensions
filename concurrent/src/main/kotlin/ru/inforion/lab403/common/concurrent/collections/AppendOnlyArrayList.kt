package ru.inforion.lab403.common.concurrent.collections

import java.io.Serializable
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock
import kotlin.NoSuchElementException
import kotlin.concurrent.withLock
import kotlin.math.max

/**
 * Concurrent ArrayList which supports only one mutable operation: appending
 */


class AppendOnlyArrayList<E>(capacity: Int = 0): List<E>, RandomAccess, Cloneable, Serializable {
    companion object {
        private val DEFAULT_CAPACITY = 10
        private val EMPTY_ELEMENTDATA = arrayOf<Any>()
        private val GROW_FACTOR = 1.7
    }

    private val futureSize = AtomicInteger(0)
    private val currentSize = AtomicInteger(0)
    private var data = EMPTY_ELEMENTDATA as Array<E>

    private val growLock = ReentrantLock()

    override val size: Int
        get() = currentSize.get()

    init {
        if (capacity > 0) {
            data = arrayOfNulls<Any?>(capacity) as Array<E>
        }
    }

    private fun updateSize(size: Int) {
        currentSize.getAndAccumulate(size, Math::max)// need for synchronize array size, MB to slow
    }

    @Synchronized private fun grow(minCapacity: Int) = growLock.withLock {
        if (minCapacity > data.size) { //very important check
            data = Arrays.copyOf(data, newCapacity(minCapacity))
        }
    }

    @Synchronized private fun grow() = grow(data.size + 1)

    private fun newCapacity(minCapacity: Int) =
        if (data.isEmpty()) { max(DEFAULT_CAPACITY, minCapacity) }
        else { max((data.size * GROW_FACTOR).toInt(), minCapacity) }

    fun add(element: E) {
        val index = futureSize.getAndIncrement()
        if (index >= data.size) { grow(index + 1) }
        data[index] = element
        updateSize(index + 1)
    }

    internal fun unsafeAddAll(elements: Collection<E?>): Boolean { //isn't concurrent
        if (elements.isEmpty()) { return false}
        val toAdd = (elements as Collection<Any?>).toTypedArray() as Array<E?> //WTF Kotlin?
        val startIndex = futureSize.getAndAdd(toAdd.size)
        if (startIndex > data.size) { grow(size) }
        System.arraycopy(toAdd, 0, data, startIndex, toAdd.size)
        updateSize(startIndex + toAdd.size)
        return true
    }

    override fun contains(element: E) = data.contains(element)

    override fun containsAll(elements: Collection<E>) = elements.all { data.contains(it) }

    override fun get(index: Int) = data[index]

    override fun indexOf(element: E) = data.indexOf(element)

    override fun isEmpty() = size == 0

    override fun iterator() = subList(0, size).iterator()

    override fun lastIndexOf(element: E) = data.lastIndexOf(element)

    override fun listIterator() = TODO()

    override fun listIterator(index: Int) = TODO()

    override fun subList(fromIndex: Int, toIndex: Int) = SubList(data, fromIndex, toIndex)

    class SubList<E>(internal val data: Array<E>, internal val start: Int, internal val end: Int): List<E> {
        override val size: Int
            get() = end - start

        override fun contains(element: E) = (start until end).any { data[it] == element }

        override fun containsAll(elements: Collection<E>): Boolean {
            TODO("Not yet implemented")
        }

        override fun get(index: Int) = data[start + index]

        override fun indexOf(element: E) = (start until end).first { data[it] == element }

        override fun isEmpty() = start == end

        override fun iterator() = SubListIterator(this)

        override fun lastIndexOf(element: E): Int {
            TODO("Not yet implemented")
        }

        override fun listIterator(): ListIterator<E> {
            TODO("Not yet implemented")
        }

        override fun listIterator(index: Int): ListIterator<E> {
            TODO("Not yet implemented")
        }

        override fun subList(fromIndex: Int, toIndex: Int) = SubList(data, fromIndex + start, toIndex + start)
    }
    class SubListIterator<E>(private val list: SubList<E>): Iterator<E> {
        private var id = list.start
        override fun hasNext() = id < list.end

        override fun next(): E {
            if (id >= list.end) throw NoSuchElementException()
            return list.data[id++]
        }
    }
}

fun<E> Collection<E>.toAppendOnlyArrayListOf(): AppendOnlyArrayList<E> {
    val array = AppendOnlyArrayList<E>(size)
    array.unsafeAddAll(this)
    return array
}

fun<E> appendOnlyArrayListOf(vararg args: E): AppendOnlyArrayList<E> {
    val array = AppendOnlyArrayList<E>(args.size)
    args.forEach { array.add(it) }
    return array
}