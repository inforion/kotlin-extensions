package ru.inforion.lab403.common.logging.config

import ru.inforion.lab403.common.logging.LogLevel
import ru.inforion.lab403.common.logging.publishers.AbstractPublisher
import ru.inforion.lab403.common.logging.publishers.BeautyPublisher
import java.io.File
import java.util.*

class LoggerConfigStringConverter  {
    data class PublisherInfo(
        val cls: String,
        val args: List<Any>? = null,
    )

    data class LoggerInfo(
        val level: String? = null,
        val publishers: MutableList<String>? = null,
        val additivity: Boolean = true,
    )

    data class ConfigurationInfo(
        val publishers: Map<String, PublisherInfo>,
        val loggers: Map<String, LoggerInfo>,
    )

    data class LoggerRuntimeInfo(
        private var customLevel: LogLevel? = null,
        var publishers: MutableList<AbstractPublisher>? = null,
        var additivity: Boolean = true,
    ) {
        // Це тоже непонятно зачем, пока решил оставить, но потом уберу мб
        var level: LogLevel?
            get() = customLevel
            set(value) {
                customLevel = value
            }

        fun addPublisher(publisher: AbstractPublisher) {
            publishers = (publishers ?: mutableListOf())
                .also { it.add(publisher) }
        }

        fun removePublisher(publisher: AbstractPublisher) {
            publishers?.remove(publisher)
        }
    }


}


fun LoggerConfigStringConverter.PublisherInfo.toPublisher(): AbstractPublisher {
    when(cls){
        "STDOUT" -> return BeautyPublisher.stdout()
        "STDERR" -> return BeautyPublisher.stderr()
        else -> {
            val cls = Class.forName(cls)
            val args = args?.toTypedArray()
            val types = args?.map { arg ->
                when (val type = arg.javaClass) {
                    java.lang.Byte::class.java -> Byte::class.java
                    java.lang.Short::class.java -> Short::class.java
                    java.lang.Integer::class.java -> Int::class.java
                    java.lang.Long::class.java -> Long::class.java
                    java.lang.Boolean::class.java -> Boolean::class.java
                    else -> type
                }
            }?.toTypedArray()
            val constructor = cls.getConstructor(*types!!)
            return constructor.newInstance(*args) as AbstractPublisher
        }
    }
}