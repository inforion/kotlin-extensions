package ru.inforion.lab403.common.logging.publishers

import ru.inforion.lab403.common.logging.ALL
import ru.inforion.lab403.common.logging.LogLevel
import ru.inforion.lab403.common.logging.formatters.Absent
import ru.inforion.lab403.common.logging.formatters.IFormatter
import ru.inforion.lab403.common.logging.formatters.Informative
import ru.inforion.lab403.common.logging.permit
import java.io.File
import java.io.FileWriter
import java.io.OutputStream
import java.io.PrintStream
import java.io.Writer

open class PrintStreamBeautyPublisher(
    name: String,
    val stream: PrintStream,
    flushEnabled: Boolean = false,
    formatter: IFormatter = Absent(),
) : WriterBeautyPublisher(name, stream.writer(), flushEnabled, formatter);