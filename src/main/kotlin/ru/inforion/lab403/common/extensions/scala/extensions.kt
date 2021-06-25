@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions.scala

import org.jetbrains.kotlinx.spark.api.map
import scala.Tuple2

inline val <K, V> Iterable<Tuple2<K, V>>.keys get() = map { it._1 }
inline val <K, V> Iterable<Tuple2<K, V>>.values get() = map { it._2 }

inline val <K, V> Iterator<Tuple2<K, V>>.keys get() = map { it._1 }
inline val <K, V> Iterator<Tuple2<K, V>>.values get() = map { it._2 }

inline val <K, V> Tuple2<K, V>.first: K get() = _1
inline val <K, V> Tuple2<K, V>.second: V get() = _2

inline operator fun <K, V> Tuple2<K, V>.component1(): K = first
inline operator fun <K, V> Tuple2<K, V>.component2(): V = second

inline fun <K, V> Tuple2<K, V>.toPair() = Pair(first, second)