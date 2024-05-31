package ru.inforion.lab403.common.logging.formatters

import ru.inforion.lab403.common.logging.*
import ru.inforion.lab403.common.logging.logger.Logger


class NotInformative(
    val nameLength: Int? = 30,
    val dateFormat: String? = "HH:mm:ss",
    val colors: Boolean = true,
) : IFormatter {
    override fun format(message: String, level: LogLevel, logger: Logger): String {
        val date = dateFormat?.let { formatDate(System.currentTimeMillis(), dateFormat) } ?: ""
        val name = nameLength?.let { stretch(logger.name, nameLength) } ?: logger.name
        return if (colors) {
            "${level.color}${level.abbreviation} $date [$name] $message$colorResetChar\n"
        } else {
            "${level.abbreviation} $date [$name] $message\n"
        }
    }
}
