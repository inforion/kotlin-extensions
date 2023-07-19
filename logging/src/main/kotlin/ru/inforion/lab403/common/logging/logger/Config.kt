package ru.inforion.lab403.common.logging.logger

import ru.inforion.lab403.common.logging.INFO
import ru.inforion.lab403.common.logging.LogLevel
import ru.inforion.lab403.common.logging.publishers.AbstractPublisher

object Config {
    data class PublisherInfo(val cls: String, val args: List<Any>)

    /**
     * Used only in ConfigFileLoader, seems redundant
     */
    data class LoggerInfo(val level: String? = null, val publishers: MutableList<PublisherInfo>? = null)

    data class LoggerRuntimeInfo(
        private var customLevel: LogLevel? = null,
        var publishers: MutableList<AbstractPublisher>? = null
    ) {
        var level: LogLevel
            get() = customLevel ?: level("all")
            set(value) {
                customLevel = value
            }

        fun addPublisher(publisher: AbstractPublisher) {
            if (publishers == null)
                publishers = mutableListOf()
            publishers!!.add(publisher)

        }

        fun removePublisher(publisher: AbstractPublisher) {
            if (publishers != null)
                publishers!!.remove(publisher)

        }
    }

    fun PublisherInfo.toPublisher(): AbstractPublisher {
        val cls = Class.forName(cls)
        val args = args.toTypedArray()
        val types = args.map { arg ->
            when (val type = arg.javaClass) {
                java.lang.Byte::class.java -> Byte::class.java
                java.lang.Short::class.java -> Short::class.java
                java.lang.Integer::class.java -> Int::class.java
                java.lang.Long::class.java -> Long::class.java
                java.lang.Boolean::class.java -> Boolean::class.java
                else -> type
            }
        }.toTypedArray()
        val constructor = cls.getConstructor(*types)
        return constructor.newInstance(*args) as AbstractPublisher
    }

    private val mapOfLoggerRuntimeInfo = initMapOfLoggers()

    private fun initMapOfLoggers() = mutableMapOf(
        "all" to LoggerRuntimeInfo(INFO, publishers = mutableListOf(Logger.defaultPublisher))
    )

    /**
     * @since 0.2.4
     */
    fun level(name: String): LogLevel = mapOfLoggerRuntimeInfo[name]?.level ?: mapOfLoggerRuntimeInfo["all"]!!.level

    /**
     * @since 0.2.4
     */
    fun publishers(
        mask: String?
    ): List<AbstractPublisher> {
        val suitableLoggers = getLoggerNamesByMask(mask)
        if (suitableLoggers.isEmpty())
            return mapOfLoggerRuntimeInfo["all"]!!.publishers!!
        return suitableLoggers
            .flatMap { mapOfLoggerRuntimeInfo[it]?.publishers ?: mapOfLoggerRuntimeInfo["all"]!!.publishers!! }
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
    fun addPublisher(publisher: AbstractPublisher, mask: String? = null) {
        changeLoggersByMask(mask) { loggerName ->
            if (mapOfLoggerRuntimeInfo[loggerName] != null)
                mapOfLoggerRuntimeInfo[loggerName]!!.addPublisher(publisher)
            else mapOfLoggerRuntimeInfo[loggerName] = LoggerRuntimeInfo(publishers = mutableListOf(publisher))
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
    fun removePublisher(publisher: AbstractPublisher, mask: String? = null) {
        changeLoggersByMask(mask) { loggerName ->
            if (mapOfLoggerRuntimeInfo[loggerName] != null)
                mapOfLoggerRuntimeInfo[loggerName]!!.removePublisher(publisher)
            else {
                val runtimeInfo = LoggerRuntimeInfo(publishers = publishers("all").toMutableList())
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
    fun changeLevel(level: LogLevel, mask: String? = null) {
        changeLoggersByMask(mask) { loggerName ->
            if (mapOfLoggerRuntimeInfo[loggerName] != null)
                mapOfLoggerRuntimeInfo[loggerName]!!.level = level
            else {
                mapOfLoggerRuntimeInfo[loggerName] = LoggerRuntimeInfo(level)
            }
        }
    }

    /**
     * Expect mask as `*.package.*`, with at most 2 asterisks (at the start and at the end)
     * `*` or null is for selecting every logger
     */
    private inline fun changeLoggersByMask(
        mask: String?,
        changeFunction: (name: String) -> Unit
    ) {
        val suitableLoggers = getLoggerNamesByMask(mask)
        for (name in suitableLoggers) {
            changeFunction(name)
        }
    }

    private inline fun getLoggerNamesByMask(mask: String?): List<String> {
        if (mask == null || mask == "*")
            return listOf("all")
        val modifiedMask = Regex(mask.replace(".", "\\.").replace("*", ".*"))
        return Logger.loggers.map { it.key }.filter { it.matches(modifiedMask) }
    }
}