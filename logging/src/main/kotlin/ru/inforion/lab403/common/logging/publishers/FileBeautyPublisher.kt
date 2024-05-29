package ru.inforion.lab403.common.logging.publishers

import ru.inforion.lab403.common.logging.formatters.Absent
import ru.inforion.lab403.common.logging.formatters.IFormatter
import java.io.FileWriter

open class FileBeautyPublisher(
    name: String,
    val filepath: String,
    val append: Boolean = false,
    flushEnabled: Boolean = false,
    formatter: IFormatter = Absent(),
) : WriterBeautyPublisher(name, FileWriter(filepath, append), flushEnabled, formatter)