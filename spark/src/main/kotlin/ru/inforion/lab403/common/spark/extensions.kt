@file:Suppress("unused")

package ru.inforion.lab403.common.spark

import org.apache.spark.api.java.JavaRDD
import org.apache.spark.api.java.JavaSparkContext

inline fun <reified T> List<T>.partition(count: Int): List<List<T>> {
    val sizeAndStep = size / count
    return windowed(sizeAndStep, sizeAndStep, partialWindows = true)
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