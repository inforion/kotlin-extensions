@file:Suppress("MemberVisibilityCanBePrivate")

package ru.inforion.lab403.common.logging.formatters

import ru.inforion.lab403.common.extensions.isWindowsOperatingSystem
import ru.inforion.lab403.common.logging.color
import ru.inforion.lab403.common.logging.logger.Record
import ru.inforion.lab403.common.logging.misc.Colors

object ColorMultiline: Formatter {

    private val resetChar = if (isWindowsOperatingSystem) Colors.ANSI_NONE else Colors.ANSI_RESET

    private fun String.paint(color: String) = "${color}$this$resetChar"

    override fun format(message: String, record: Record): String {
        val lines = message.lines()
        val color = record.level.color
        return lines.joinToString(separator = "\n") { it.paint(color) }
    }
}