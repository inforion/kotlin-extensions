package ru.inforion.lab403.common.logging.publishers

import org.slf4j.LoggerFactory
import ru.inforion.lab403.common.logging.LogLevel
import ru.inforion.lab403.common.logging.logger.Logger
import ru.inforion.lab403.common.logging.tests.SLF4JLoggerTest


class TestPublisherWithSlf4J : AbstractPublisher("TestPublisherWithSlf4J", false) {
    val log = LoggerFactory.getLogger(SLF4JLoggerTest::class.java)

    override fun publish(message: String, level: LogLevel, logger: Logger) {
        log.info(message)
    }

    override fun flush() = Unit
}