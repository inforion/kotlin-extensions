@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.logging.logger

import ru.inforion.lab403.common.logging.*
import ru.inforion.lab403.common.logging.storage.LoggerStorage
import ru.inforion.lab403.common.logging.publishers.AbstractPublisher
import java.util.logging.Level
import kotlin.concurrent.thread

class Logger private constructor(
    val name: String,
) {
//    init {
//        assert(
//            !name.contains(NAME_REGEXP)
//        ) { "Logger name shouldn't contain Regex special characters" }
//    }

    var level: LogLevel
        get() {
            return cacheLevel ?: LoggerStorage.collectLevel(name).also {
                this.cacheLevel = it
            }
        }
        set(value) {
            LoggerStorage.setLevel(name, value)
            cacheLevel = value
        }

    private var cacheLevel: LogLevel? = null
    private var cachePublishers: List<AbstractPublisher>? = null

    fun invalidate() {
        cacheLevel = null
        cachePublishers = null
    }

    fun hasCache() = !((cachePublishers == null) && (cacheLevel == null))

    companion object {
//        val NAME_REGEXP = Regex("[\\[\\]{}()+*?^\$\\\\|]")

        private val runtime = Runtime.getRuntime()

        /**
         * Shutdown hook to flush all loggers when program exit
         */
        private val shutdownHook = thread(false) { flush() }.also { runtime.addShutdownHook(it) }

        /**
         * Create new logger by name with specified publishers or get it (logger) if it already exists for the class
         *
         * NOTE: this function is more likely internal API, please use functions from 'loggers.kt'
         *
         * @param name name of the new logger
         * @param level level of logging message below it will not be published
         * @param publishers list of publishers
         *
         * @since 0.2.0
         */
        fun create(
            name: String,
            level: LogLevel? = null,
        ): Logger {
            return LoggerStorage.loggers[name] ?: run {
                val logName = validateLoggerName(name)
                val newLogger = Logger(logName)
                LoggerStorage.loggers[logName] = newLogger
                if (level != null)
                    LoggerStorage.setLevel(logName, level)

                newLogger
            }
        }

        /**
         * Create new logger by class with specified publishers or get it (logger) if it already exist for the class
         *
         * NOTE: this function is more likely internal API, please use functions from 'loggers.kt'
         *
         * @param klass class to use to get name of the new logger
         * @param level level of logging message below it will not be published
         * @param publishers list of publishers
         *
         * @since 0.2.0
         */
        fun <T> create(
            klass: Class<T>,
            level: LogLevel? = null,
        ) =
            create(klass.name, level)

        /**
         * Flush all publishers of all loggers
         *
         * @since 0.2.0
         */
        fun flush() = LoggerStorage.loggers.values.forEach { it.flush() }
    }

    var stackFrameOffset = 0

    /**
     * Union sequence of own and shared handlers
     */
    private val allPublishers
        get() = cachePublishers ?: LoggerStorage.collectPublishers(name).also {
            cachePublishers = it
        }

    override fun toString() = name

    /**
     * Flush all publishers of logger
     *
     * @since 0.2.0
     */
    fun flush() = allPublishers.forEach { it.flush() }

    @PublishedApi
    internal fun log(level: LogLevel, flush: Boolean, message: String) {
        allPublishers.forEach {
            it.prepareAndPublish(message, level, this)
            if (flush) {
                it.flush()
            } else {
                it.checkAndFlush()
            }
        }
    }

    /**
     * Log message using defined publishers in logger
     *
     * @param level message log level
     * @param message message supplier
     *
     * @since 0.2.0
     */
    inline fun <T : Any> log(level: LogLevel, flush: Boolean = false, message: Messenger<T>) {
        if (this.level permit level) log(level, flush, message().toString())
    }

    @Deprecated("please use log(level: LogLevel, ...)")
    inline fun <T : Any> log(level: Level, flush: Boolean = false, message: Messenger<T>) =
        log(level.logLevel(), flush, message)

    /**
     * Emits a lazy severe log [message] (score = 1000)
     *
     * @param flush if true message will be emitted immediately
     *
     * @since 0.2.0
     */
    inline fun <T : Any> severe(flush: Boolean = false, message: Messenger<T>) = log(SEVERE, flush, message)

    /**
     * Emits a lazy warning log [message] (score = 900)
     *
     * @param flush if true message will be emitted immediately
     *
     * @since 0.2.0
     */
    inline fun <T : Any> warning(flush: Boolean = false, message: Messenger<T>) = log(WARNING, flush, message)

    /**
     * Emits a lazy info log [message] (score = 800)
     *
     * @param flush if true message will be emitted immediately
     *
     * @since 0.2.0
     */
    inline fun <T : Any> info(flush: Boolean = false, message: Messenger<T>) = log(INFO, flush, message)

    /**
     * Emits a lazy config log [message] (score = 700)
     *
     * @param flush if true message will be emitted immediately
     *
     * @since 0.2.0
     */
    inline fun <T : Any> config(flush: Boolean = false, message: Messenger<T>) = log(CONFIG, flush, message)

    /**
     * Emits a lazy fine log [message] (score = 500)
     *
     * @param flush if true message will be emitted immediately
     *
     * @since 0.2.0
     */
    inline fun <T : Any> fine(flush: Boolean = false, message: Messenger<T>) = log(FINE, flush, message)

    /**
     * Emits a lazy finer log [message] (score = 400)
     *
     * @param flush if true message will be emitted immediately
     *
     * @since 0.2.0
     */
    inline fun <T : Any> finer(flush: Boolean = false, message: Messenger<T>) = log(FINER, flush, message)

    /**
     * Emits a lazy finest log [message] (score = 300)
     *
     * @param flush if true message will be emitted immediately
     *
     * @since 0.2.0
     */
    inline fun <T : Any> finest(flush: Boolean = false, message: Messenger<T>) = log(FINEST, flush, message)

    /**
     * Emits a lazy debug log [message] (score = 200)
     *
     * @param flush if true message will be emitted immediately
     *
     * @since 0.2.0
     */
    inline fun <T : Any> debug(flush: Boolean = false, message: Messenger<T>) = log(DEBUG, flush, message)

    /**
     * Emits a lazy trace log [message] (score = 100)
     *
     * @param flush if true message will be emitted immediately
     *
     * @since 0.2.0
     */
    inline fun <T : Any> trace(flush: Boolean = false, message: Messenger<T>) = log(TRACE, flush, message)
}