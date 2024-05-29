package ru.inforion.lab403.common.logging

import ru.inforion.lab403.common.logging.logger.Logger
import java.util.logging.Level
import kotlin.reflect.full.companionObject

/**
 * Unwraps companion class to enclosing class given a Java Class
 */
internal fun <T: Any> unwrapCompanionClass(ofClass: Class<T>) = if (ofClass.enclosingClass != null &&
        ofClass.enclosingClass.kotlin.companionObject?.java == ofClass) ofClass.enclosingClass else ofClass

/**
 * Returns logger for Java class, if companion object fix the name
 *
 * @param forClass class to create logger for
 * @param level logger level
 * @param flush flush message when publish it
 * @param publishers publishers list
 */
internal fun <T: Any> logger(
    forClass: Class<T>,
    level: LogLevel? = null,
): Logger {
    val klass = unwrapCompanionClass(forClass)
    return Logger.create(klass, level)
}

/**
 * Creates logger with specified publisher list [publishers] or get existed and
 *   returns logger from extended class (or the enclosing class)
 *
 * @param level logger level
 * @param flush flush message when publish it
 * @param publishers publishers list
 */
fun <T: Any> T.logger(
    level: LogLevel? = null,
) = logger(javaClass, level)

/**
 * Returns logger from extended class (or the enclosing class)
 */
@Deprecated("please use logger(level: LogLevel, ...)")
fun <T: Any> T.logger(level: Level) = logger(javaClass, level.logLevel())
