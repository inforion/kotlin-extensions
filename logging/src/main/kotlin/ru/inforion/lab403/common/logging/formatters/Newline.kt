package ru.inforion.lab403.common.logging.formatters

import ru.inforion.lab403.common.logging.LogLevel
import ru.inforion.lab403.common.logging.logger.Logger

class Newline : IFormatter {
    override fun format(message: String, level: LogLevel, logger: Logger): String = buildString {
        appendLine(message)
    }
}