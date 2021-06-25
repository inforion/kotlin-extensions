@file:Suppress("unused")

package ru.inforion.lab403.common.extensions.spark

import org.apache.spark.api.java.JavaRDD
import org.apache.spark.sql.SparkSession
import org.jetbrains.kotlinx.spark.api.dsOf
import org.jetbrains.kotlinx.spark.api.toDS

inline fun <reified T> List<T>.parallelize(session: SparkSession): JavaRDD<T> = session.toDS(this).toJavaRDD()

inline fun <reified T> Array<T>.parallelize(session: SparkSession): JavaRDD<T> = session.dsOf(*this).toJavaRDD()

inline val <T> Iterator<T>.list get() = mutableListOf<T>().apply { while (hasNext()) this += next() }

fun <T, R> Iterator<T>.mapIndexed(index: Int = 0, func: (Int, T) -> R) = MappingIterator(this, func, index)

inline fun <T, R> Iterator<T>.forEachIndexed(index: Int = 0, func: (Int, T) -> R) {
    var current = index
    forEach { func(current++, it) }
}

inline val String.sparkEnv get() = "spark.executorEnv.$this"