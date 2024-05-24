package ru.inforion.lab403.common.logging.storage

import ru.inforion.lab403.common.logging.INFO
import ru.inforion.lab403.common.logging.LogLevel
import ru.inforion.lab403.common.logging.config.ILoggerConfigInitializer
import ru.inforion.lab403.common.logging.config.LoggerConfigStringConverter
import ru.inforion.lab403.common.logging.config.LoggerFileConfigInitializer
import ru.inforion.lab403.common.logging.logger.Logger
import ru.inforion.lab403.common.logging.publishers.AbstractPublisher
import java.util.*

object LoggerStorage {
    private const val DEFAULT_LEVEL = INFO
    const val ALL = "."
    private val loggerFileConfigInitializer: ILoggerConfigInitializer = LoggerFileConfigInitializer()

    /**
     * Initial configurations
     */
    private fun initMapOfLoggers() = mutableMapOf(
        ALL to LoggerConfigStringConverter.LoggerRuntimeInfo(
            DEFAULT_LEVEL,
            publishers = mutableListOf(Logger.defaultPublisher)
        )
    )

    /**
     * Logger's configurations map
     */
    private val mapOfLoggerRuntimeInfo = initMapOfLoggers()

    /**
     * All already created loggers
     */
    internal val loggers = mutableMapOf<String, Logger>()

    init {
        //loggerFileConfigInitializer.load()

        val loader = ServiceLoader.load(ILoggerConfigInitializer::class.java)
        loader.forEach { it.load() }
    }

    /**
     * @since 0.2.4
     */
    fun level(name: String = ALL): LogLevel {
        val prefixes = name.split('.')
        return (prefixes.size downTo 1)
            .map { prefixes.take(it).joinToString(".") }
            .firstNotNullOfOrNull { mapOfLoggerRuntimeInfo[it]?.level } ?: mapOfLoggerRuntimeInfo[ALL]?.level!! // Переделать!
    }

    /**
     * @since 0.2.4
     */
    fun publishers(name: String = ALL): List<AbstractPublisher> {
        if (name == ALL) return mapOfLoggerRuntimeInfo[name]?.publishers!!
        val publishersSet = publishers().toMutableSet()

        if (name.isNotEmpty()) {
            val prefixes = name.split('.')
            var i = prefixes.size
            do {
                val subPath = prefixes.take(i).joinToString(".")
                val conf = mapOfLoggerRuntimeInfo[subPath]
                conf?.publishers?.forEach {
                    publishersSet.add(it)
                }
                i--
            } while ((i != 0) and ((conf == null) or (conf?.additivity == true))) //?
        }
        return publishersSet.toList()
    }

    /**
     * Add new publisher to loggers
     *
     * @param publisher publisher to add
     * @param name name of the logger
     *
     * @since 0.4.TODO
     */
    fun addPublisher(name: String, publisher: AbstractPublisher) {
        if (name.isNotBlank()) {
            loggers[name]?.invalidate()
            val loggerInfo = mapOfLoggerRuntimeInfo.getOrPut(name) {
                LoggerConfigStringConverter.LoggerRuntimeInfo()
            }
            loggerInfo.addPublisher(publisher)
        }
    }

    /**
     * Remove publisher from loggers
     *
     * @param publisher publisher to remove
     * @param name name of the logger
     *
     * @since 0.4.TODO
     */
    fun removePublisher(name: String, publisher: AbstractPublisher? = null) {
        loggers[name]?.invalidate()

        if (publisher != null) {
            mapOfLoggerRuntimeInfo[name]?.removePublisher(publisher)
        } else {
            mapOfLoggerRuntimeInfo[name]?.publishers?.clear()
        }
    }

    fun clearPublishers() {
        loggers.forEach{
            it.value.invalidate()
        }
        mapOfLoggerRuntimeInfo.clear()
        mapOfLoggerRuntimeInfo.putAll(initMapOfLoggers())
    }

    fun clearLoggers() {
        loggers.clear()
    }

    /**
     * Change log level of loggers
     *
     * @param level new log level
     * @param name name of the logger
     *
     * @since 0.4.TODO
     */
    fun setLevel(name: String, level: LogLevel) {
        if (name.isNotBlank()) {
            loggers[name]?.invalidate()
            if (mapOfLoggerRuntimeInfo.contains(name))
                mapOfLoggerRuntimeInfo[name]?.level = level
            else mapOfLoggerRuntimeInfo[name] = LoggerConfigStringConverter.LoggerRuntimeInfo(level)
        }
    }

    fun addLoggerInfo(name: String, level: LogLevel?, publishers: MutableList<AbstractPublisher>?, additivity: Boolean) {
        mapOfLoggerRuntimeInfo[name] = LoggerConfigStringConverter.LoggerRuntimeInfo(level, publishers, additivity)
    }

    fun setAdditivity(name: String, additivity: Boolean) {
        if (name.isNotBlank()) {
            loggers[name]?.invalidate()
            if (mapOfLoggerRuntimeInfo.contains(name))
                mapOfLoggerRuntimeInfo[name]?.additivity = additivity
            else mapOfLoggerRuntimeInfo[name] = LoggerConfigStringConverter.LoggerRuntimeInfo(additivity = additivity)
        }
    }

    fun getAllInfo() = mapOfLoggerRuntimeInfo.toMap()

    fun getLoggerConfigurationsString(): String  = buildString {
        appendLine("Current Logger Configurations:")
        appendLine("----------------------------")
        mapOfLoggerRuntimeInfo.forEach { (name, info) ->
            appendLine("Logger Name: $name")
            appendLine("    Level: ${info.level}")
            val publishers = info.publishers?.joinToString(", ") { it.name } ?: "No publishers"
            appendLine("    Publishers: $publishers")
            appendLine("    Additivity: ${info.additivity}")
            appendLine("----------------------------")
        }
    }
}