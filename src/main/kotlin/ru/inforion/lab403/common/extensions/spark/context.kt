@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions.spark

import org.apache.spark.SparkContext
import org.apache.spark.SparkExecutorInfo
import org.apache.spark.broadcast.Broadcast
import org.jetbrains.kotlinx.spark.api.KSparkSession
import org.jetbrains.kotlinx.spark.api.sparkContext
import ru.inforion.lab403.common.spark.SparkFuckingScalaAbstracter

inline fun <reified T> SparkContext.broadcast(value: T): Broadcast<T> = broadcast(value) { T::class.java }

inline fun <reified T> T.broadcast(session: KSparkSession) = session.spark.sparkContext.broadcast(this)

inline val SparkContext.executors: List<String> get() = SparkFuckingScalaAbstracter.getExecutors(this)

inline val SparkContext.executorInfos: Array<SparkExecutorInfo> get() = statusTracker().executorInfos

fun SparkExecutorInfo.stringify() = "Info(" +
        "host=${host()} " +
        "port=${port()} " +
        "tasks=${numRunningTasks()} " +
        "cache=${cacheSize()} " +
        "total=${totalOnHeapStorageMemory()} " +
        "used=${usedOnHeapStorageMemory()})"