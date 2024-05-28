package ru.inforion.lab403.common.logging.storage

import ru.inforion.lab403.common.logging.INFO
import ru.inforion.lab403.common.logging.LogLevel
import ru.inforion.lab403.common.logging.config.ILoggerConfigInitializer
import ru.inforion.lab403.common.logging.config.LoggerConfigStringConverter
import ru.inforion.lab403.common.logging.config.LoggerFileConfigInitializer
import ru.inforion.lab403.common.logging.logger.Logger
import ru.inforion.lab403.common.logging.publishers.AbstractPublisher
import ru.inforion.lab403.common.logging.publishers.BeautyPublisher

/**
 * LoggerStorage object used to store and control configuration
 * of all loggers
 */
object LoggerStorage {

    /**
     * Default logger's configurations
     */
    val defaultPublisher: AbstractPublisher = BeautyPublisher.stdout()
    const val DEFAULT_LEVEL = INFO
    const val ALL = ""

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
     * @since 0.2.4
     */
    fun level(name: String = ALL): LogLevel {
        // TODO: optimize, убрать take, сделать через индексы
        val prefixes = name.split('.')
        return (prefixes.size downTo 1)
            .map { prefixes.take(it).joinToString(".") }
            .firstNotNullOfOrNull { mapOfLoggerRuntimeInfo[it]?.level } ?: DEFAULT_LEVEL
    }

    /**
     * @since 0.2.4
     */
    fun publishers(name: String = ALL): List<AbstractPublisher> {
        val publishersSet = mutableSetOf<AbstractPublisher>()

        // TODO (лень открывать ютрек): а точно этот вариант нужно рассматривать?
        // Он, кажется, покроется while'ом
        // TODO: как вариант, хранить set не из всех паблишеров, а только из названий
        // TODO: а сами паблишеры возвращать через yield
        // TODO: можно это побенчмаркать даже
        if (name == ALL) {
            mapOfLoggerRuntimeInfo[ALL]?.publishers?.let { publishersSet.addAll(it) }
            return publishersSet.toList()
        }

        val dotIndices = name.indices.filter { name[it] == '.' }.toMutableList()
        dotIndices.add(name.length)

        var i = dotIndices.size

        // TODO: why not for-loop?
        while (i > 0) {
            val subPath = name.substring(0, dotIndices[i - 1])
            val conf = mapOfLoggerRuntimeInfo[subPath]
            conf?.publishers?.let { publishersSet.addAll(it) }
            if (conf?.additivity == false) break
            i--
        }

        return publishersSet.toList()
    }


    /**
     * Add new publisher to loggers
     *
     * @param publisher publisher to add
     * @param name name of the logger or module/package with loggers
     *
     * @since 0.4.8
     */
    fun addPublisher(name: String, publisher: AbstractPublisher) {
        // loggers[name]?.invalidate()
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
        // loggers[name]?.invalidate()
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
        loggers.forEach{
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
        //loggers[name]?.invalidate()
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
        //loggers[name]?.invalidate()
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
    fun invalidateLoggersCacheByName(name: String){
        loggers.filter { it.key.contains(name) }.map { it.value.invalidate() }
    }

    fun addLoggerInfo(name: String, level: LogLevel?, publishers: MutableList<AbstractPublisher>?, additivity: Boolean) {
        mapOfLoggerRuntimeInfo[name] = LoggerConfigStringConverter.LoggerRuntimeInfo(level, publishers, additivity)
    }

    fun getAllInfo() = mapOfLoggerRuntimeInfo.toMap()


    /**
     * Returns all configs as String
     *
     * @since 0.4.8
     */
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