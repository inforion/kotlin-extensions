package ru.inforion.lab403.common.logging.storage

import ru.inforion.lab403.common.logging.INFO
import ru.inforion.lab403.common.logging.LogLevel
import ru.inforion.lab403.common.logging.config.ILoggerConfigInitializer
import ru.inforion.lab403.common.logging.config.LoggerConfigStringConverter
import ru.inforion.lab403.common.logging.config.LoggerFileConfigInitializer
import ru.inforion.lab403.common.logging.formatters.Informative
import ru.inforion.lab403.common.logging.logger.Logger
import ru.inforion.lab403.common.logging.publishers.AbstractPublisher
import ru.inforion.lab403.common.logging.publishers.PrintStreamBeautyPublisher
import ru.inforion.lab403.common.logging.publishers.StdoutBeautyPublisher
import ru.inforion.lab403.common.logging.publishers.fallback.AbstractFallbackPublisher
import ru.inforion.lab403.common.logging.publishers.fallback.StderrFallbackPublisher

/**
 * LoggerStorage object used to store and control configuration
 * of all loggers
 */
object LoggerStorage {
    /**
     * Default logger's configurations
     */
    val defaultPublisher: AbstractPublisher = StdoutBeautyPublisher(formatter = Informative())
    const val DEFAULT_LEVEL = INFO
    const val ALL = ""

    val defaultFallbackPublisher = StderrFallbackPublisher()
    val fallbackPublishers = mutableListOf<AbstractFallbackPublisher>(defaultFallbackPublisher)

    /**
     * Initial configurations
     */
    private fun initMapOfLoggers() = mutableMapOf(
        ALL to LoggerConfigStringConverter.LoggerRuntimeInfo(
            DEFAULT_LEVEL,
            publishers = mutableListOf(defaultPublisher)
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

    /**
     * loggerFileConfigInitializer used to load configuration from json file
     */
    private val loggerFileConfigInitializer: ILoggerConfigInitializer = LoggerFileConfigInitializer()

    init {
        loggerFileConfigInitializer.load()

//        val loader = ServiceLoader.load(ILoggerConfigInitializer::class.java)
//        loader.forEach { it.load() }
    }

    /**
     * @since 0.4.8
     */
    fun takeRuntimeInfoWhile(name: String, callback: (LoggerConfigStringConverter.LoggerRuntimeInfo) -> Boolean) {
        val dotIndices = name.indices.filter { name[it] == '.' }.toMutableList()
        dotIndices.add(name.length)

        for (i in dotIndices.reversed()) {
            val subPath = name.substring(0, i)
            val conf = mapOfLoggerRuntimeInfo[subPath]
            if (conf != null && !callback(conf)) break
        }
    }

    /**
     * Returns LogLevel of logger, that will be used for logging
     *
     * @param name name of the logger or module/package with loggers
     *
     * @since 0.2.4
     */
    fun collectLevel(name: String): LogLevel {
        var result: LogLevel? = null
        takeRuntimeInfoWhile(name) taker@{ conf ->
            result = conf.level
            return@taker result == null
        }
        return result ?: DEFAULT_LEVEL
    }

    /**
     * Returns publishers list of logger, that will be used for logging
     *
     * @param name name of the logger or module/package with loggers
     *
     * @since 0.2.4
     */
    fun collectPublishers(name: String = ALL): List<AbstractPublisher> {
        val publishersSet = mutableSetOf<AbstractPublisher>()
        takeRuntimeInfoWhile(name) taker@{ conf ->
            conf.publishers?.let { publishersSet.addAll(it) }
            return@taker conf.additivity
        }
        return publishersSet.toList()
    }

    /**
     * Get LogLevel of logger from configuration map by his name
     *
     * @param name name of the logger or module/package with loggers
     *
     * @since 0.4.8
     */
    fun getLevel(name: String) = mapOfLoggerRuntimeInfo[name]?.level

    /**
     * Get publishers of logger from configuration map by his name
     *
     * @param name name of the logger or module/package with loggers
     *
     * @since 0.4.8
     */
    fun getPublishers(name: String) = mapOfLoggerRuntimeInfo[name]?.publishers

    /**
     * Add new publisher to loggers
     *
     * @param publisher publisher to add
     * @param name name of the logger or module/package with loggers
     *
     * @since 0.4.8
     */
    fun addPublisher(name: String, publisher: AbstractPublisher) {
        invalidateLoggersCacheByName(name)
        val loggerInfo = mapOfLoggerRuntimeInfo.getOrPut(name) {
            LoggerConfigStringConverter.LoggerRuntimeInfo()
        }
        loggerInfo.addPublisher(publisher)
    }

    /**
     * Remove publisher from loggers
     *
     * @param publisher publisher to remove
     * @param name name of the logger or module/package with loggers
     *
     * @since 0.4.8
     */
    fun removePublisher(name: String, publisher: AbstractPublisher? = null) {
        invalidateLoggersCacheByName(name)
        if (publisher != null) {
            mapOfLoggerRuntimeInfo[name]?.removePublisher(publisher)
        } else {
            mapOfLoggerRuntimeInfo[name]?.publishers?.clear()
        }
    }

    /**
     * Clear all publishers from loggers
     *
     * @since 0.4.8
     */
    fun clearPublishers() {
        loggers.forEach {
            it.value.invalidate()
        }
        mapOfLoggerRuntimeInfo.clear()
        mapOfLoggerRuntimeInfo.putAll(initMapOfLoggers())
    }

    /**
     * Clear all loggers
     *
     * @since 0.4.8
     */
    fun clearLoggers() {
        loggers.clear()
    }

    /**
     * Change log level of loggers
     *
     * @param level new log level
     * @param name name of the logger or module/package with loggers
     *
     * @since 0.4.8
     */
    fun setLevel(name: String, level: LogLevel) {
        invalidateLoggersCacheByName(name)
        if (mapOfLoggerRuntimeInfo.contains(name))
            mapOfLoggerRuntimeInfo[name]?.level = level
        else mapOfLoggerRuntimeInfo[name] = LoggerConfigStringConverter.LoggerRuntimeInfo(level)
    }

    /**
     * Change additivity of loggers
     * Additivity means that logger will inherit publishers of his ancestors
     *
     * @param additivity new additivity
     * @param name name of the logger or module/package with loggers
     *
     * @since 0.4.8
     */
    fun setAdditivity(name: String, additivity: Boolean) {
        invalidateLoggersCacheByName(name)
        if (mapOfLoggerRuntimeInfo.contains(name))
            mapOfLoggerRuntimeInfo[name]?.additivity = additivity
        else mapOfLoggerRuntimeInfo[name] = LoggerConfigStringConverter.LoggerRuntimeInfo(additivity = additivity)
    }

    /**
     * Invalidate cache of the logger or loggers, which are included in name of module/package
     *
     * @param name name of the logger or module/package with loggers
     *
     * @since 0.4.8
     */
    fun invalidateLoggersCacheByName(name: String) {
        // TODO: проверить
        loggers.filter { it.key.contains(name) }.map { it.value.invalidate() }
    }

    fun addLoggerInfo(
        name: String,
        level: LogLevel?,
        publishers: MutableList<AbstractPublisher>?,
        additivity: Boolean
    ) {
        mapOfLoggerRuntimeInfo[name] = LoggerConfigStringConverter.LoggerRuntimeInfo(level, publishers, additivity)
    }

    fun addFallbackPublisher(publisher: AbstractFallbackPublisher) {
        fallbackPublishers.add(publisher)
    }

    fun removeFallbackPublisher(publisher: AbstractFallbackPublisher) {
        fallbackPublishers.remove(publisher)
    }

    fun iterateFallbackPublishers() = fallbackPublishers.asIterable()

    fun getAllInfo() = mapOfLoggerRuntimeInfo.toMap()

    /**
     * Returns all configs as String
     *
     * @since 0.4.8
     */
    fun getLoggerConfigurationsString(): String = buildString {
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