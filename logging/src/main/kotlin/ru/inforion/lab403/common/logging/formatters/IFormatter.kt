package ru.inforion.lab403.common.logging.formatters

import ru.inforion.lab403.common.logging.logger.Logger
import ru.inforion.lab403.common.logging.LogLevel

fun interface IFormatter {
    fun format(message: String, level: LogLevel, logger: Logger): String
}