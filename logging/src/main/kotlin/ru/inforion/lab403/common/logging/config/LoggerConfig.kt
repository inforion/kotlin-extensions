package ru.inforion.lab403.common.logging.config

import ru.inforion.lab403.common.logging.INFO
import ru.inforion.lab403.common.logging.LogLevel
import ru.inforion.lab403.common.logging.logger.Logger
import ru.inforion.lab403.common.logging.publishers.AbstractPublisher

object LoggerConfig {
    const val DEFAULT_LEVEL = INFO
    const val ALL = "*"


    // TODO: а есть ли смысл вообще это хранить в одной мапе? Мб на две разделить
    // UseCase: например, два паблишера и десять лог-левелов. Если одна мапа, то на каждый паблишер надо будет
    private val mapOfLoggerRuntimeInfo = initMapOfLoggers()

    private fun initMapOfLoggers() = mutableMapOf(
        ALL to LoggerConfigStringConverter.LoggerRuntimeInfo(INFO, publishers = mutableListOf(Logger.defaultPublisher))
    )

    /**
     * @since 0.2.4
     */
    fun level(name: String = ALL): LogLevel = mapOfLoggerRuntimeInfo[name]?.level ?: mapOfLoggerRuntimeInfo[ALL]!!.level

    /**
     * @since 0.2.4
     */
    fun publishers(mask: String = ALL): List<AbstractPublisher> {
        val suitableLoggers = getLoggerNamesByMask(mask)
        if (suitableLoggers.isEmpty())
            return mapOfLoggerRuntimeInfo[ALL]!!.publishers!!
        return suitableLoggers
            .flatMap { mapOfLoggerRuntimeInfo[it]?.publishers ?: mapOfLoggerRuntimeInfo[ALL]!!.publishers!! }
            .toSet().toList()
    }

    /**
     * Add new publisher to loggers
     *
     * @param publisher publisher to add
     * @param mask mask of suitable loggers.
     *
     * @since 0.4.TODO
     */
    fun addPublisher(publisher: AbstractPublisher, mask: String = ALL) {
        changeLoggersByMask(mask) { loggerName ->
            if (mapOfLoggerRuntimeInfo[loggerName] != null)
                mapOfLoggerRuntimeInfo[loggerName]!!.addPublisher(publisher)
            else
                mapOfLoggerRuntimeInfo[loggerName] =
                    LoggerConfigStringConverter.LoggerRuntimeInfo(publishers = mutableListOf(publisher))

            // TODO: а какой в результате у нас инвариант? Мы не кэшируем всё, а в рантайме достаём? Так это же будет медленнее
            //  Вот тут если не было данных, то родительские паблишеры не будут работать
        }
    }

    /**
     * Remove publisher from loggers
     *
     * @param publisher publisher to remove
     * @param mask mask of suitable loggers
     *
     * @since 0.4.TODO
     */
    fun removePublisher(publisher: AbstractPublisher, mask: String = ALL) {
        changeLoggersByMask(mask) { loggerName ->
            if (mapOfLoggerRuntimeInfo[loggerName] != null)
                mapOfLoggerRuntimeInfo[loggerName]!!.removePublisher(publisher)
            else {
                val runtimeInfo =
                    LoggerConfigStringConverter.LoggerRuntimeInfo(publishers = publishers(ALL).toMutableList())
                runtimeInfo.removePublisher(publisher)
                mapOfLoggerRuntimeInfo[loggerName] = runtimeInfo
            }
        }
    }

    fun clearPublishers() {
        mapOfLoggerRuntimeInfo.clear()
        mapOfLoggerRuntimeInfo.putAll(initMapOfLoggers())
    }

    /**
     * Change log level of loggers
     *
     * @param level new log level
     * @param mask mask of suitable loggers
     *
     * @since 0.4.TODO
     */
    fun changeLevel(level: LogLevel, mask: String = ALL) {
        changeLoggersByMask(mask) { loggerName ->
            if (mapOfLoggerRuntimeInfo[loggerName] != null)
                mapOfLoggerRuntimeInfo[loggerName]!!.level = level
            else
                mapOfLoggerRuntimeInfo[loggerName] = LoggerConfigStringConverter.LoggerRuntimeInfo(level)
        }
    }

    /**
     * Expect mask as `*.package.*`, with at most 2 asterisks (at the start and at the end)
     * `*` or null is for selecting every logger
     */
    private fun changeLoggersByMask(mask: String, changeFunction: (name: String) -> Unit) {
        val suitableLoggers = getLoggerNamesByMask(mask)
        for (name in suitableLoggers)
            changeFunction(name)
    }

    private fun getLoggerNamesByMask(mask: String): List<String> {
        if (mask == ALL)
            return listOf(ALL)
        val modifiedMask = Regex(
            mask.replace(".", "\\.")
                .replace("*", ".*")
        )
        return Logger.loggers.map { it.key }.filter { it.matches(modifiedMask) }
    }
}