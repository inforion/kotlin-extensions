package ru.inforion.lab403.common.concurrent.collections

import ru.inforion.lab403.common.extensions.cast
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
    private var data: Array<E> = EMPTY_ELEMENTDATA.cast()

    private val growLock = ReentrantLock()

    override val size: Int
        get() = currentSize.get()

    init {
        if (capacity > 0) {
            data = arrayOfNulls<Any?>(capacity).cast()
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

    internal fun unsafeAddAll(elements: Collection<E>): Boolean { //isn't concurrent
        if (elements.isEmpty()) return false
        val toAdd = elements.cast<Collection<Any?>>().toTypedArray()
        val startIndex = futureSize.getAndAdd(elements.size)
        if (startIndex > data.size) { grow(size) }
        System.arraycopy(toAdd, 0, data, startIndex, toAdd.size)
        updateSize(startIndex + toAdd.size)
        return true
    }

    override fun contains(element: E) = subList(0, size).contains(element)

    override fun containsAll(elements: Collection<E>) = toSubList().containsAll(elements)

    override fun get(index: Int) = if (index < size) data[index] else throw IndexOutOfBoundsException()

    override fun indexOf(element: E) = toSubList().indexOf(element)

    override fun isEmpty() = size == 0

    override fun iterator() = listIterator()

    override fun lastIndexOf(element: E) = toSubList().lastIndexOf(element)

    override fun listIterator() = toSubList().listIterator()

    override fun listIterator(index: Int) = toSubList().listIterator(index)

    override fun subList(fromIndex: Int, toIndex: Int) = SubList(data, fromIndex, toIndex)

    fun toSubList(start: Int = 0) = subList(start, size)

    class SubList<E>(internal val data: Array<E>, internal val start: Int, internal val end: Int): List<E> {
        override val size = end - start

        override fun contains(element: E) = (start until end).any { data[it] == element }

        override fun containsAll(elements: Collection<E>): Boolean {
            TODO("Not yet implemented")
        }

        override fun get(index: Int) = if (index < size) data[index] else throw IndexOutOfBoundsException()

        override fun indexOf(element: E) = (start until end).first { data[it] == element }

        override fun isEmpty() = start == end

        override fun iterator() = listIterator()

        override fun lastIndexOf(element: E) = ((end-1)..start).first { data[it] == element }

        override fun listIterator() = SubListIterator(this)

        override fun listIterator(index: Int) = SubListIterator(this, index)

        override fun subList(fromIndex: Int, toIndex: Int) = SubList(data, fromIndex + start, toIndex + start)
    }
    class SubListIterator<E>(private val list: SubList<E>, start: Int = 0): ListIterator<E> {
        private var id = list.start + start
        override fun hasNext() = id < list.end

        override fun next(): E {
            if (id >= list.end) throw NoSuchElementException()
            return list.data[id++]
        }

        override fun hasPrevious() = id > list.start

        override fun nextIndex() = list.start - id

        override fun previous(): E {
            if (id <= list.start) throw NoSuchElementException()
            return list.data[--id]
        }

        override fun previousIndex() = list.start - id - 1
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