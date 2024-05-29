package ru.inforion.lab403.common.logging.formatters

import ru.inforion.lab403.common.logging.LogLevel
import ru.inforion.lab403.common.logging.logger.Logger

class Absent : IFormatter {
    override fun format(message: String, level: LogLevel, logger: Logger): String = message
}