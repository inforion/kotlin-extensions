@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions.spark

import org.apache.spark.sql.SparkSession
import org.jetbrains.kotlinx.spark.api.SparkLogLevel
import org.jetbrains.kotlinx.spark.api.setLogLevel
import org.jetbrains.kotlinx.spark.api.sparkContext
import ru.inforion.lab403.common.extensions.toULong

fun sparkSession(
        props: Map<String, Any> = emptyMap(),
        master: String = "local[*]",
        appName: String = "Kotlin Spark Sample",
        logLevel: SparkLogLevel = SparkLogLevel.ERROR
): SparkSession = SparkSession
        .builder()
        .master(master)
        .appName(appName)
        .apply {
            props.forEach {
                when (val value = it.value) {
                    is String -> config(it.key, value)
                    is Boolean -> config(it.key, value)
                    is Int -> config(it.key, value.toULong())
                    is Long -> config(it.key, value)
                    is Double -> config(it.key, value)
                    else -> throw IllegalArgumentException("Cannot set property ${it.key} because value $value of unsupported type ${value::class}")
                }
            }
        }.orCreate
    .also {
        it.sparkContext.setLogLevel(logLevel)
    }

fun <T> shield(value: T) = value