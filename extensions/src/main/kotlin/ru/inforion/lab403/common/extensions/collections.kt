@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions

inline fun <T>Iterable<T?>.forEachNotNull(block: (T) -> Unit) = forEach {
    if (it != null) block(it)
}

inline fun <T>Array<T?>.forEachNotNull(block: (T) -> Unit) = forEach {
    if (it != null) block(it)
}

@Deprecated("just use range(...) since 0.3.4")
inline fun collect(count: Int) = (0 until count).toList()

/**
 * Returns sequence with total specified element [count] and [block] initializer for each
 *
 * @since 0.3.4
 */
inline fun <T> sequence(count: Int, crossinline block: (Int) -> T) = sequence { repeat(count) { yield(block(it)) } }

/**
 * Returns a single map of all elements from all maps in the given collection.
 *
 * @since 0.3.4
 */
inline fun <T, R>Collection<Map<T, R>>.flatten(): Map<T, R> = mutableMapOf<T, R>().also { map -> forEach { map += it } }

/**
 * Returns a map where keys are indexes of items of original list [this] and values are items
 *
 * @since 0.3.4
 */
inline fun <T> List<T>.associateByIndex(): Map<Int, T> = mutableMapOf<Int, T>().also {
    forEachIndexed { index, item -> it[index] = item }
}

/**
 * Returns a mutable list of nulls with specified generic type
 *
 * NOTE: arrayOfNulls require reified T parameter and thus all upper parameter must be reified
 *       that will lead to expose all private non-inline function
 *
 * @since 0.3.5
 */
@Suppress("UNCHECKED_CAST")
inline fun <T> mutableListOfNulls(size: Int) = Array<Any?>(size) { null }.asList() as MutableList<T?>

inline val <T> List<T>.start get() = first()

inline val <T> List<T>.startOrNull get() = firstOrNull()

inline val <T> List<T>.end get() = last()

inline val <T> List<T>.endOrNull get() = lastOrNull()

inline fun <reified T> List<T>.split(count: Int): List<List<T>> {
    require(size > 0) { "List is empty" }
    require(count > 0) { "Amount of partitions must be positive" }

    if (size == 1)
        return listOf(this)

    val result = mutableListOf<List<T>>()

    var index = 0
    var timer = size % count
    var step = size / count + if (timer > 0) 1 else 0
    repeat (if (count > size) size else count) {
        result.add(subList(index, index + step).toList())
        index += step
        timer -= 1
        if (timer == 0) step -= 1
    }

    return result
}

inline fun <R> List<*>.findInstance(klass: Class<R>): R? {
    for (element in this) if (klass.isInstance(element)) return element.cast()
    return null
}

inline fun <R> List<*>.firstInstance(klass: Class<R>): R {
    for (element in this) if (klass.isInstance(element)) return element.cast()
    throw NoSuchElementException()
}


inline fun <R> List<*>.hasInstance(klass: Class<R>): Boolean {
    for (element in this) if (klass.isInstance(element)) return true
    return false
}

inline fun <T> Iterator<T>.toList(): List<T> {
    val result = mutableListOf<T>()
    while (hasNext()) result.add(next())
    return result
}

inline fun <T, C: Collection<T>> C.ifNotEmpty(action: (C) -> Unit) {
    if (isNotEmpty()) action(this)
}

inline fun <IK, IV, OK, OV> Map<IK, IV>.associate(transform: (Map.Entry<IK, IV>) -> Pair<OK, OV>) = entries.associate(transform)

inline fun <K, V> Map<K, V>.ifContains(key: K, action: (V) -> Unit) = get(key) ifNotNull action

inline fun <T, R : Comparable<R>> Iterable<T>.minBy(selector: (T) -> R) =
    minByOrNull(selector) ?: error("Collection should not be empty")

inline fun <T, R : Comparable<R>> Iterable<T>.maxBy(selector: (T) -> R) =
    maxByOrNull(selector) ?: error("Collection should not be empty")

inline val <A, B> Collection<Pair<A, B>>.firsts get() = map { it.first }

inline val <A, B> Collection<Pair<A, B>>.seconds get() = map { it.second }

inline val <reified A, B> Array<out Pair<A, B>>.firsts get() = map { it.first }.toTypedArray()

inline val <A, reified B> Array<out Pair<A, B>>.seconds get() = map { it.second }.toTypedArray()