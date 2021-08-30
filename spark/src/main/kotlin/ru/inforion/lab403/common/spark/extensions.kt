@file:Suppress("unused")

package ru.inforion.lab403.common.spark

import org.apache.spark.api.java.JavaRDD
import org.apache.spark.api.java.JavaSparkContext

inline fun <reified T> List<T>.partition(count: Int): List<List<T>> {
    require(size > 0) { "List is empty" }

    if (size == 1)
        return listOf(this)

    val chunked = chunked(size / count)

    if (chunked.size > count) {
        val tail = chunked.last()
        val body = chunked.dropLast(1)

        val result = ArrayList<ArrayList<T>>()

        tail.forEachIndexed { index, element ->
            val part = ArrayList<T>()
            part.addAll(body[index])
            part.add(element)
            result.add(part)
        }
        body.takeLast(size % count).forEach { part ->
            result.add(part as ArrayList<T>)
        }
        return result.toList()
    }
    return chunked
}

inline fun <reified T> List<T>.parallelize(sc: JavaSparkContext): JavaRDD<T> = sc.parallelize(this)

inline fun <reified T> Array<T>.parallelize(sc: JavaSparkContext): JavaRDD<T> =
    sc.parallelize(toList())

inline val <T> Iterator<T>.list get() = mutableListOf<T>().apply { while (hasNext()) this += next() }

fun <T, R> Iterator<T>.mapIndexed(index: Int = 0, func: (Int, T) -> R) = MappingIterator(this, func, index)

inline fun <T, R> Iterator<T>.forEachIndexed(index: Int = 0, func: (Int, T) -> R) {
    var current = index
    forEach { func(current++, it) }
}

inline val String.sparkEnv get() = "spark.executorEnv.$this"