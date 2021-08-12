package ru.inforion.lab403.common.jline

import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.terminal.TerminalBuilder

const val escapeChar = "`"

fun jline(): LineReader = LineReaderBuilder.builder()
    .terminal(TerminalBuilder.terminal())
    .build()

fun LineReader.waitUntilReturn(): String = readLine("Press ENTER to exit...")

fun LineReader.processUntilReturn(block: (String) -> String?) {
    while (true) {
        val command = readLine("Listening commands (Press Enter to exit...)")
        if (command != "") {
            val result = block(command)
            if (result != null)
                println("$escapeChar${result}$escapeChar")
        }
        else
            return
    }
}