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
 * Concurrent ULongArrayList which supports only one mutable operation: appending
 */


class AppendOnlyULongArray(capacity: Int = 0): List<ULong>, RandomAccess, Cloneable, Serializable {
    companion object {
        private const val DEFAULT_CAPACITY = 10
        private val EMPTY_ELEMENTDATA = ulongArrayOf()
        private const val GROW_FACTOR = 2
    }

    private val futureSize = AtomicInteger(0)
    private val currentSize = AtomicInteger(0)
    private var data: ULongArray = EMPTY_ELEMENTDATA.cast()

    private val growLock = ReentrantLock()

    override val size: Int
        get() = currentSize.get()

    init {
        if (capacity > 0) {
            data = ULongArray(capacity)
        }
    }

    private fun updateSize(size: Int) {
        currentSize.getAndAccumulate(size, Math::max)// need for synchronize array size, MB to slow
    }

    private fun grow(minCapacity: Int) = growLock.withLock {
        if (minCapacity > data.size) { //very important check
            data = data.copyOf(newCapacity(minCapacity))
        }
    }

    private fun grow() = grow(data.size + 1)

    private fun newCapacity(minCapacity: Int) =
        if (data.isEmpty()) { max(DEFAULT_CAPACITY, minCapacity) }
        else { max((data.size * GROW_FACTOR), minCapacity) }

    fun add(element: ULong) {
        val index = futureSize.getAndIncrement()
        if (index >= data.size) { grow(index + 1) }
        data[index] = element
        updateSize(index + 1)
    }

    internal fun unsafeAddAll(elements: Collection<ULong>): Boolean { //isn't concurrent
        if (elements.isEmpty()) return false
        val toAdd = elements.cast<Collection<Any?>>().toTypedArray()
        val startIndex = futureSize.getAndAdd(elements.size)
        if (startIndex > data.size) { grow(size) }
        System.arraycopy(toAdd, 0, data, startIndex, toAdd.size)
        updateSize(startIndex + toAdd.size)
        return true
    }

    override fun contains(element: ULong) = subList(0, size).contains(element)

    override fun containsAll(elements: Collection<ULong>) = toSubList().containsAll(elements)

    override fun get(index: Int) = if (index < size) data[index] else throw IndexOutOfBoundsException()

    override fun indexOf(element: ULong) = toSubList().indexOf(element)

    override fun isEmpty() = size == 0

    override fun iterator() = listIterator()

    override fun lastIndexOf(element: ULong) = toSubList().lastIndexOf(element)

    override fun listIterator() = toSubList().listIterator()

    override fun listIterator(index: Int) = toSubList().listIterator(index)

    override fun subList(fromIndex: Int, toIndex: Int) = SubList(data, fromIndex, toIndex)

    fun toSubList(start: Int = 0) = subList(start, size)

    class SubList(internal val data: ULongArray, internal val start: Int, internal val end: Int): List<ULong> {
        override val size = end - start

        override fun contains(element: ULong) = (start until end).any { data[it] == element }

        override fun containsAll(elements: Collection<ULong>): Boolean {
            TODO("Not yet implemented")
        }

        override fun get(index: Int) = if (index < size) data[index] else throw IndexOutOfBoundsException()

        override fun indexOf(element: ULong) = (start until end).first { data[it] == element }

        override fun isEmpty() = start == end

        override fun iterator() = listIterator()

        override fun lastIndexOf(element: ULong) = ((end-1)..start).first { data[it] == element }

        override fun listIterator() = SubListIterator(this)

        override fun listIterator(index: Int) = SubListIterator(this, index)

        override fun subList(fromIndex: Int, toIndex: Int) = SubList(data, fromIndex + start, toIndex + start)
    }
    class SubListIterator(private val list: SubList, start: Int = 0): ListIterator<ULong> {
        private var id = list.start + start
        override fun hasNext() = id < list.end

        override fun next(): ULong {
            if (id >= list.end) throw NoSuchElementException()
            return list.data[id++]
        }

        override fun hasPrevious() = id > list.start

        override fun nextIndex() = list.start - id

        override fun previous(): ULong {
            if (id <= list.start) throw NoSuchElementException()
            return list.data[--id]
        }

        override fun previousIndex() = list.start - id - 1
    }
}

fun Collection<ULong>.toAppendOnlyULongArray(): AppendOnlyULongArray {
    val array = AppendOnlyULongArray(size)
    array.unsafeAddAll(this)
    return array
}

fun appendOnlyULongArrayOf(vararg args: ULong): AppendOnlyULongArray {
    val array = AppendOnlyULongArray(args.size)
    args.forEach { array.add(it) }
    return array
}