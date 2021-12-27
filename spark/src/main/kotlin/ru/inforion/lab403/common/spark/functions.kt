@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.spark

import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.serializer.KryoRegistrator
import org.apache.spark.serializer.KryoSerializer
import org.apache.spark.serializer.Serializer
import ru.inforion.lab403.common.extensions.ifNotNull
import ru.inforion.lab403.common.extensions.javaClassPath
import kotlin.reflect.KClass

//@Deprecated("SparkSession not work with KryoSerializer correctly")
//fun sparkSession(
//        props: Map<String, Any> = emptyMap(),
//        master: String = "local[*]",
//        appName: String = "Kotlin Spark Sample",
//        logLevel: SparkLogLevel = SparkLogLevel.ERROR
//): SparkSession = SparkSession
//        .builder()
//        .master(master)
//        .appName(appName)
//        .apply {
//            props.forEach {
//                when (val value = it.value) {
//                    is String -> config(it.key, value)
//                    is Boolean -> config(it.key, value)
//                    is Int -> config(it.key, value.long_z)
//                    is Long -> config(it.key, value)
//                    is Double -> config(it.key, value)
//                    else -> throw IllegalArgumentException("Cannot set property ${it.key} because value $value of unsupported type ${value::class}")
//                }
//            }
//        }.orCreate
//    .also {
//        it.sparkContext.setLogLevel(logLevel)
//    }

inline fun getClasspathJars(classpath: String = javaClassPath) = classpath.split(":").toTypedArray()

inline fun sparkSerializerConf(
    serializer: KClass<out Serializer> = KryoSerializer::class,
    registrator: KClass<out KryoRegistrator>? = null,
    registrationRequired: Boolean = false
): Map<String, Any> {
    val result = mutableMapOf<String, Any>()

    result["spark.serializer"] = serializer.java.name

    registrator ifNotNull { result["spark.kryo.registrator"] = java.name }

    result["spark.kryo.registrationRequired"] = registrationRequired

    return result
}

inline fun sparkStreamingConf(
    backpressureInitialRate: Long?,

    backpressureEnabled: Boolean,

    kafkaMaxRatePerPartition: Long?,
    kafkaMinRatePerPartition: Long?
) : Map<String, Any> {
    val result = mutableMapOf<String, Any>()

    result["spark.streaming.backpressure.enabled"] = backpressureEnabled

    backpressureInitialRate ifNotNull { result["spark.streaming.backpressure.initialRate"] = this }
    kafkaMaxRatePerPartition ifNotNull { result["spark.streaming.kafka.maxRatePerPartition"] = this }
    kafkaMinRatePerPartition ifNotNull { result["spark.streaming.kafka.minRatePerPartition"] = this }

    return result
}

fun sparkContext(
    appName: String,
    properties: Map<String, Any> = emptyMap(),
    environment: Map<String, Any> = emptyMap(),
    master: String = "local[*]",
    jars: Array<String>? = null,
    logLevel: String = "ERROR"
): JavaSparkContext {
    val conf = SparkConf()
        .setAppName(appName)
        .setMaster(master)

    properties.forEach { (key, value) -> conf.set(key, value.toString()) }
    environment.forEach { (key, value) -> conf.setExecutorEnv(key, value.toString()) }

    jars ifNotNull { conf.setJars(this) }

    return JavaSparkContext(conf)
        .apply { setLogLevel(logLevel) }
}

inline fun JavaSparkContext.waitForParallelism(coresCount: Int, additionalAction: (Int) -> Unit) {
    do {
        Thread.sleep(250)
        val level = defaultParallelism()
        additionalAction(level)
    } while ( level < coresCount)
}

fun <T> shield(value: T) = value