package ru.inforion.lab403.common.logging.publishers

import ru.inforion.lab403.common.logging.formatters.Absent
import ru.inforion.lab403.common.logging.formatters.IFormatter
import java.io.FileWriter
import java.io.Writer

open class WriterBeautyPublisher(
    name: String,
    val writer: Writer,
    flushEnabled: Boolean = false,
    formatter: IFormatter = Absent(),
) : AbstractBeautyPublisher(name, flushEnabled, formatter) {
    override fun publishPrepared(message: String) {
        writer.write(message)
    }

    override fun flush() {
        writer.flush()
    }

}