package ru.inforion.lab403.common.jline

import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.terminal.TerminalBuilder

fun jline(): LineReader = LineReaderBuilder.builder()
    .terminal(TerminalBuilder.terminal())
    .build()

fun LineReader.waitUntilReturn(): String = readLine("Press ENTER to exit...")
