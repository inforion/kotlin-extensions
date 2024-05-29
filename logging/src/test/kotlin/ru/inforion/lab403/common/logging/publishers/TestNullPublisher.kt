package ru.inforion.lab403.common.logging.publishers

import ru.inforion.lab403.common.logging.LogLevel
import ru.inforion.lab403.common.logging.logger.Logger
import java.util.UUID


class TestNullPublisher(name: String) : AbstractPublisher(name, false) {
    override fun publish(message: String, level: LogLevel, logger: Logger) = Unit

    override fun flush() = Unit
}

fun generateTestNullPublisher() =
    TestNullPublisher("TestNullPublisher " + UUID.randomUUID().toString())