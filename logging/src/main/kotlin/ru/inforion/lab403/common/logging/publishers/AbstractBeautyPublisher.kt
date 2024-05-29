package ru.inforion.lab403.common.logging.publishers

import ru.inforion.lab403.common.logging.LogLevel
import ru.inforion.lab403.common.logging.formatters.IFormatter
import ru.inforion.lab403.common.logging.logger.Logger

abstract class AbstractBeautyPublisher(
    name: String,
    flushEnabled: Boolean,
    open val formatter: IFormatter
): AbstractPublisher(name, flushEnabled) {
    override fun publish(message: String, level: LogLevel, logger: Logger) {
        publishPrepared(formatter.format(message, level, logger))
    }

    abstract fun publishPrepared(message: String)
}