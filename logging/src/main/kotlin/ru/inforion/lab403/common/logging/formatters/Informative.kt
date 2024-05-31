package ru.inforion.lab403.common.logging.formatters

import ru.inforion.lab403.common.logging.*
import ru.inforion.lab403.common.logging.logger.Logger

private object defaultFormatCallback : InformativeFormatCallback {
    override fun callback(
        message: String,
        level: LogLevel,
        name: String,
        time: Long,
        location: StackTraceElement?,
        nameLength: Int,
        locationLength: Int,
        dateFormat: String,
        color: Boolean
    ) = buildString {
        // "%(time) %(level) %(location) [%(name)]: %(message)\n"

        if (color) {
            append(level.color)
        }
        append(formatDate(time, dateFormat))
        append(" ")
        append(level.abbreviation)
        append(" ")
        stretchInline(location?.toString() ?: "NO_LOC_INFO", locationLength) { append(it) }
        append(" [")
        stretchInline(name, nameLength) { append(it) }
        append("]: ")
        append(message)

        if (color) {
            appendLine(colorResetChar)
        } else {
            appendLine()
        }
    }
}

fun interface InformativeFormatCallback {
    fun callback(
        message: String,
        level: LogLevel,
        name: String,
        time: Long,
        location: StackTraceElement?,
        nameLength: Int,
        locationLength: Int,
        dateFormat: String,
        color: Boolean,
    ): String
}

class Informative(
    val stackElementNegativeIndex: Int = 8,
    val nameLength: Int = 20,
    val locationLength: Int = 50,
    val dateFormat: String = "HH:mm:ss",
    val colors: Boolean = true,
    val formatCallback: InformativeFormatCallback = defaultFormatCallback
) : IFormatter {
    override fun format(message: String, level: LogLevel, logger: Logger): String {
        return formatCallback.callback(
            message,
            level,
            logger.name,
            millisecondsCollector(),
            stackTraceElementCollector(stackElementNegativeIndex),
            nameLength,
            locationLength,
            dateFormat,
            colors
        )
    }
}