package ru.inforion.lab403.common.logging.publishers

import ru.inforion.lab403.common.logging.formatters.Absent
import ru.inforion.lab403.common.logging.formatters.IFormatter
import ru.inforion.lab403.common.logging.formatters.NotInformative

class StdoutBeautyPublisher(
    name: String = "STDOUT",
    flushEnabled: Boolean = true,
    formatter: IFormatter = NotInformative()
) : PrintStreamBeautyPublisher(name, System.out, flushEnabled, formatter)

class StderrBeautyPublisher(
    name: String = "STDERR",
    flushEnabled: Boolean = true,
    formatter: IFormatter = NotInformative()
) : PrintStreamBeautyPublisher(name, System.err, flushEnabled, formatter)