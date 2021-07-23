@file:Suppress("NOTHING_TO_INLINE", "unused")

package unsigned.arrays

import unsigned.types.*
import java.io.Serializable

@JvmInline
value class UIntArray @PublishedApi internal constructor(
    @PublishedApi internal val storage: IntArray
) : Collection<UInt>, Serializable {

    /** Creates a new array of the specified [size], with all elements initialized to zero. */
    constructor(size: Int) : this(IntArray(size))

    /**
     * Returns the array element at the given [index]. This method can be called using the index operator.
     *
     * If the [index] is out of bounds of this array, throws an [IndexOutOfBoundsException] except in Kotlin/JS
     * where the behavior is unspecified.
     */
    inline operator fun get(index: Int): ULong = storage[index].toULong()

    /**
     * Sets the element at the given [index] to the given [value]. This method can be called using the index operator.
     *
     * If the [index] is out of bounds of this array, throws an [IndexOutOfBoundsException] except in Kotlin/JS
     * where the behavior is unspecified.
     */
    operator fun set(index: Int, value: UInt) {
        storage[index] = value.toInt()
    }

    /** Returns the number of elements in the array. */
    override val size get() = storage.size

    /** Creates an iterator over the elements of the array. */
    override operator fun iterator() = object : Iterator<UInt> {
        private var index = 0
        override fun hasNext() = index < storage.size
        override fun next() =
            if (index < storage.size) storage[index++].toUInt() else throw NoSuchElementException(index.toString())
    }

    override fun contains(element: UInt): Boolean {
        // TODO: Eliminate this check after KT-30016 gets fixed.
        // Currently JS BE does not generate special bridge method for this method.
        @Suppress("USELESS_CAST")
        if ((element as Any?) !is UInt) return false

        return storage.contains(element.toInt())
    }

    override fun containsAll(elements: Collection<UInt>) =
        (elements as Collection<*>).all { it is ULong && storage.contains(it.toInt()) }

    override fun isEmpty() = storage.isEmpty()
}


