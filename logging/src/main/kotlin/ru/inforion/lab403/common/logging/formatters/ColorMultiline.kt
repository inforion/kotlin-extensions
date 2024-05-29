@file:Suppress("MemberVisibilityCanBePrivate")

package ru.inforion.lab403.common.logging.formatters

import ru.inforion.lab403.common.extensions.isWindowsOperatingSystem
import ru.inforion.lab403.common.logging.LogLevel
import ru.inforion.lab403.common.logging.color
import ru.inforion.lab403.common.logging.logger.Logger
import ru.inforion.lab403.common.logging.misc.Colors

val colorResetChar = Colors.ANSI_RESET

class ColorMultiline : IFormatter {
    override fun format(message: String, level: LogLevel, logger: Logger): String {
        val lines = message.lines()
        val color = level.color
        return buildString {
            lines.forEach { line ->
                append(color)
                append(line)
                appendLine(colorResetChar)
            }
        }
    }
}