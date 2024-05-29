package ru.inforion.lab403.common.logging.publishers

import ru.inforion.lab403.common.logging.LogLevel
import ru.inforion.lab403.common.logging.logger.Logger


class TestPublisher(private val value0: Int, private val value1: String) :
    AbstractPublisher("TestPublisher", flushEnabled = false) {
    override fun publish(message: String, level: LogLevel, logger: Logger) {
        println("message: $message, value0: $value0, value1: $value1, level: $level, logger: $logger")
    }

    override fun flush() = Unit
}