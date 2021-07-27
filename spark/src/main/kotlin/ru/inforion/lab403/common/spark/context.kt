@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.spark

import org.apache.spark.SparkContext
import org.apache.spark.SparkExecutorInfo
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.broadcast.Broadcast

inline fun <reified T> T.broadcast(sc: JavaSparkContext): Broadcast<T> = sc.broadcast(this)

inline val SparkContext.executors: List<String> get() = SparkScalaAbstracter.getExecutors(this)

inline val SparkContext.executorInfos: Array<SparkExecutorInfo> get() = statusTracker().executorInfos

fun SparkExecutorInfo.stringify() = "Info(" +
        "host=${host()} " +
        "port=${port()} " +
        "tasks=${numRunningTasks()} " +
        "cache=${cacheSize()} " +
        "total=${totalOnHeapStorageMemory()} " +
        "used=${usedOnHeapStorageMemory()})"