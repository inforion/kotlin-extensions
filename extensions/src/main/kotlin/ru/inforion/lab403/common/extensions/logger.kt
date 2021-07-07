package ru.inforion.lab403.common.extensions

import ru.inforion.lab403.common.logging.Levels
import ru.inforion.lab403.common.logging.logger.Logger


/**
 * Parses string with format logger0=LEVEL,logger1=LEVEL or just LEVEL and set hook on [Logger.onCreate]
 *   to change levels of specified logger or all logger in application
 *
 * Typical help string for argument parser:
 *   "Set messages minimum logging level for specified loggers in format logger0=LEVEL,logger1=LEVEL\n" +
 *   "Or for all loggers if no '=' was found in value just logger level, i.e. FINE\n" +
 *   "Available levels: ${Levels.values().joinToString()}\n"
 */
fun String.loggerConfigure() = when {
    "=" !in this -> {
        val value = Levels.valueOf(this)
        Logger.onCreate { it.level = value.level }
    }

    else -> {
        val map = split(",").associate { definition ->
            val logger = definition.substringBefore("=")
            val value = definition.substringAfter("=")
            logger to Levels.valueOf(value).level
        }

        Logger.onCreate { logger ->
            logger.level = map.getOrDefault(logger.name, logger.level)
        }
    }
}