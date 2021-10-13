@file:Suppress("NOTHING_TO_INLINE", "HasPlatformType")

package ru.inforion.lab403.common.spark

import org.apache.spark.TaskContext
import org.apache.spark.api.java.JavaRDD
import org.apache.spark.api.java.JavaRDDLike
import org.apache.spark.streaming.kafka010.HasOffsetRanges
import ru.inforion.lab403.common.concurrent.launch
import ru.inforion.lab403.common.extensions.INT_MAX
import java.util.concurrent.LinkedBlockingQueue

/**
 * Changes parameters order to call with { ... } notation
 *
 * @see JavaRDD.mapPartitionsIndexed
 */
inline fun <T, R> JavaRDD<T>.mapPartitionsIndexed(
    preservesPartitioning: Boolean = false,
    noinline action: (Int, Iterator<T>) -> Iterator<R>
): JavaRDD<R> = mapPartitionsWithIndex(action, preservesPartitioning)


inline fun <T> JavaRDD<T>.forEachPartitionsIndexed(noinline action: (Int, Iterator<T>) -> Unit) =
    foreachPartition {
        val index = TaskContext.getPartitionId()
        action(index, it)
    }

/**
 * Collects items from RDD in separated thread asynchronous and return iterator to collected items
 *
 * @param capacity maximum capacity of cache queue
 *
 * @return iterator to collected items
 */
inline fun <T, U : JavaRDDLike<T, U>> JavaRDDLike<T, U>.asyncCollect(capacity: Int = INT_MAX) = object : Iterator<T> {
    private val emptyMarker = this

    private var next: Any? = emptyMarker

    private val queue = LinkedBlockingQueue<Any?>(capacity)

    override fun hasNext(): Boolean {
        next = queue.take()
        return next !== emptyMarker
    }

    override fun next(): T {
        check(next !== emptyMarker) { "Iterator has no next element!" }
        @Suppress("UNCHECKED_CAST")
        return next as T
    }

    private var index = 0

    init {
        launch {
            val iterator = toLocalIterator()
            iterator.forEach {
                println("Add to queue: ${index++}")
                queue.add(it)
            }
            println("Finished")
        }.invokeOnCompletion {
            println("Add empty marker")
            queue.add(emptyMarker)
            it?.printStackTrace()
        }
    }
}

inline fun <T, U : JavaRDDLike<T, U>> JavaRDDLike<T, U>.collectPartition(index: Int): List<T> =
    collectPartitions(intArrayOf(index)).first()

inline fun <T> JavaRDD<T>.random(fraction: Double) = sample(false, fraction, System.currentTimeMillis())

// somehow get session from rdd and get executors count
inline fun <T, S> JavaRDD<T>.sort(ascending: Boolean = false, partitions: Int = 1, noinline selector: (T) -> S): JavaRDD<T> =
    sortBy(selector, ascending, partitions)

inline fun <T> JavaRDD<T>.offsets() = (rdd() as HasOffsetRanges).offsetRanges()
