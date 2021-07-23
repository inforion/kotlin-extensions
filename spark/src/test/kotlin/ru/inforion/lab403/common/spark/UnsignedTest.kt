package ru.inforion.lab403.common.spark

internal class UnsignedTest {
    fun context() {
//        val props = dictionaryOf(
//            "spark.cores.max" to coresCount,
//            "spark.executor.memory" to memorySize,
//
//            "spark.serializer" to KRYO_SERIALIZER,
//        )
//
//        backpressureRate ifNotNull {
//            props["spark.streaming.backpressure.initialRate"] = this
//            props["spark.streaming.backpressure.enabled"] = true
//        }
//
//        kafkaMaxRatePerPartition ifNotNull { props["spark.streaming.kafka.maxRatePerPartition"] = this }  // 3000
//        kafkaMinRatePerPartition ifNotNull { props["spark.streaming.kafka.minRatePerPartition"] = this }  // 3000
//
//        sparkBindAddress ifNotNull { props["spark.driver.bindAddress"] = this }
//
//        environment[LOG4J_PROPERTIES_FILE] ifNotNull {
//            props["spark.driver.extraJavaOptions"] = "-Dlog4j.configuration=file:$this"
//            props["spark.executor.extraJavaOptions"] = "-Dlog4j.configuration=file:$this"
//        }
//        environment[ENV_CONF_PATH] ifNotNull { props[ENV_CONF_PATH.sparkEnv] = this }
//
//        if (!sparkServer.startsWith("local")) {
//            props["spark.jars"] = javaClassPath.replace(":", ",")
//            props[LOGGING_KAFKA_SERVERS.sparkEnv] = kafkaServers
//        }
//
//        return sparkSession(props = props, master = sparkServer, appName = name)
    }
}