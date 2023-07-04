package ru.inforion.lab403.common.logging.logger

import org.slf4j.Marker
import org.slf4j.helpers.MessageFormatter
import ru.inforion.lab403.common.logging.LogLevel

class Slf4jLoggerImpl(private val loggerName: String) : org.slf4j.Logger {
    /**
     * {EN} All methods point to corresponding methods of the logger {EN}
     */
    private val logger = Logger.create(loggerName, LogLevel.MIN_VALUE, false).also {
        it.useSharedHandlers = false
        it.useSLF4JHandlers = true
        it.isTopLevelLogger = false
    }

    /**
     * Replace curly braces in [format] with [args].
     *
     * Example: formatString("{} equals {}", a, b) -> "a equals b"
     */
    private fun formatString(format: String?, vararg args: Any?): String? {
        if (format == null || args.any { it == null })
            return null
        return MessageFormatter.arrayFormat(format, args).message
    }

    private fun logExceptionWithMessage(msg: String?, t: Throwable?): String? {
        if (msg == null || t == null)
            return null
        return "$msg: $t"
    }

    override fun getName(): String {
        return loggerName
    }

    override fun isTraceEnabled(): Boolean {
        return true
    }

    override fun isTraceEnabled(marker: Marker?): Boolean {
        return true
    }

    override fun trace(msg: String?) {
        traceInternal(msg)
    }

    override fun trace(format: String?, arg: Any?) {
        traceInternal(formatString(format, arg))
    }

    override fun trace(format: String?, arg1: Any?, arg2: Any?) {
        traceInternal(formatString(format, arg1, arg2))
    }

    override fun trace(format: String?, vararg arguments: Any?) {
        traceInternal(formatString(format, *arguments))
    }

    override fun trace(msg: String?, t: Throwable?) {
        traceInternal(logExceptionWithMessage(msg, t))
    }

    override fun trace(marker: Marker?, msg: String?) {
        throw NotImplementedError("Marker logging not implemented")
    }

    override fun trace(marker: Marker?, format: String?, arg: Any?) {
        trace(marker, formatString(format, arg))
    }

    override fun trace(marker: Marker?, format: String?, arg1: Any?, arg2: Any?) {
        trace(marker, formatString(format, arg1, arg2))
    }

    override fun trace(marker: Marker?, format: String?, vararg argArray: Any?) {
        trace(marker, formatString(format, *argArray))
    }

    override fun trace(marker: Marker?, msg: String?, t: Throwable?) {
        trace(marker, logExceptionWithMessage(msg, t))
    }

    override fun isDebugEnabled(): Boolean {
        return true
    }

    override fun isDebugEnabled(marker: Marker?): Boolean {
        return true
    }

    override fun debug(msg: String?) {
        debugInternal(msg)
    }

    override fun debug(format: String?, arg: Any?) {
        debugInternal(formatString(format, arg))
    }

    override fun debug(format: String?, arg1: Any?, arg2: Any?) {
        debugInternal(formatString(format, arg1, arg2))
    }

    override fun debug(format: String?, vararg arguments: Any?) {
        debugInternal(formatString(format, *arguments))
    }

    override fun debug(msg: String?, t: Throwable?) {
        debugInternal(logExceptionWithMessage(msg, t))
    }

    override fun debug(marker: Marker?, msg: String?) {
        throw NotImplementedError("Marker logging not implemented")
    }

    override fun debug(marker: Marker?, format: String?, arg: Any?) {
        debug(marker, formatString(format, arg))
    }

    override fun debug(marker: Marker?, format: String?, arg1: Any?, arg2: Any?) {
        debug(marker, formatString(format, arg1, arg2))
    }

    override fun debug(marker: Marker?, format: String?, vararg arguments: Any?) {
        debug(marker, formatString(format, *arguments))
    }

    override fun debug(marker: Marker?, msg: String?, t: Throwable?) {
        debug(marker, logExceptionWithMessage(msg, t))
    }

    override fun isInfoEnabled(): Boolean {
        return true
    }

    override fun isInfoEnabled(marker: Marker?): Boolean {
        return true
    }

    override fun info(msg: String?) {
        infoInternal(msg)
    }

    override fun info(format: String?, arg: Any?) {
        infoInternal(formatString(format, arg))
    }

    override fun info(format: String?, arg1: Any?, arg2: Any?) {
        infoInternal(formatString(format, arg1, arg2))
    }

    override fun info(format: String?, vararg arguments: Any?) {
        infoInternal(formatString(format, *arguments))
    }

    override fun info(msg: String?, t: Throwable?) {
        infoInternal(logExceptionWithMessage(msg, t))
    }

    override fun info(marker: Marker?, msg: String?) {
        throw NotImplementedError("Marker logging not implemented")
    }

    override fun info(marker: Marker?, format: String?, arg: Any?) {
        info(marker, formatString(format, arg))
    }

    override fun info(marker: Marker?, format: String?, arg1: Any?, arg2: Any?) {
        info(marker, formatString(format, arg1, arg2))
    }

    override fun info(marker: Marker?, format: String?, vararg arguments: Any?) {
        info(marker, formatString(format, *arguments))
    }

    override fun info(marker: Marker?, msg: String?, t: Throwable?) {
        info(marker, logExceptionWithMessage(msg, t))
    }

    override fun isWarnEnabled(): Boolean {
        return true
    }

    override fun isWarnEnabled(marker: Marker?): Boolean {
        return true
    }

    override fun warn(msg: String?) {
        warnInternal(msg)
    }

    override fun warn(format: String?, arg: Any?) {
        warnInternal(formatString(format, arg))
    }

    override fun warn(format: String?, vararg arguments: Any?) {
        warnInternal(formatString(format, *arguments))
    }

    override fun warn(format: String?, arg1: Any?, arg2: Any?) {
        warnInternal(formatString(format, arg1, arg2))
    }

    override fun warn(msg: String?, t: Throwable?) {
        warnInternal(logExceptionWithMessage(msg, t))
    }

    override fun warn(marker: Marker?, msg: String?) {
        throw NotImplementedError("Marker logging not implemented")
    }

    override fun warn(marker: Marker?, format: String?, arg: Any?) {
        warn(marker, formatString(format, arg))
    }

    override fun warn(marker: Marker?, format: String?, arg1: Any?, arg2: Any?) {
        warn(marker, formatString(format, arg1, arg2))
    }

    override fun warn(marker: Marker?, format: String?, vararg arguments: Any?) {
        warn(marker, formatString(format, *arguments))
    }

    override fun warn(marker: Marker?, msg: String?, t: Throwable?) {
        warn(marker, logExceptionWithMessage(msg, t))
    }

    override fun isErrorEnabled(): Boolean {
        return true
    }

    override fun isErrorEnabled(marker: Marker?): Boolean {
        return true
    }

    override fun error(msg: String?) {
        errorInternal(msg)
    }

    override fun error(format: String?, arg: Any?) {
        errorInternal(formatString(format, arg))
    }

    override fun error(format: String?, arg1: Any?, arg2: Any?) {
        errorInternal(formatString(format, arg1, arg2))
    }

    override fun error(format: String?, vararg arguments: Any?) {
        errorInternal(formatString(format, *arguments))
    }

    override fun error(msg: String?, t: Throwable?) {
        errorInternal(logExceptionWithMessage(msg, t))
    }

    override fun error(marker: Marker?, msg: String?) {
        throw NotImplementedError("Marker logging not implemented")
    }

    override fun error(marker: Marker?, format: String?, arg: Any?) {
        error(marker, formatString(format, arg))
    }

    override fun error(marker: Marker?, format: String?, arg1: Any?, arg2: Any?) {
        error(marker, formatString(format, arg1, arg2))
    }

    override fun error(marker: Marker?, format: String?, vararg arguments: Any?) {
        error(marker, formatString(format, *arguments))
    }

    override fun error(marker: Marker?, msg: String?, t: Throwable?) {
        error(marker, logExceptionWithMessage(msg, t))
    }

    /**
     * {EN} These functions are needed to make stack size the same for each log function {EN}
     */
    private fun errorInternal(msg: String?) {
        if (msg != null)
            logger.severe { msg }
    }

    private fun infoInternal(msg: String?) {
        if (msg != null)
            logger.info { msg }
    }

    private fun traceInternal(msg: String?) {
        if (msg != null)
            logger.trace { msg }
    }

    private fun debugInternal(msg: String?) {
        if (msg != null)
            logger.debug { msg }
    }

    private fun warnInternal(msg: String?) {
        if (msg != null)
            logger.warning { msg }
    }
}
