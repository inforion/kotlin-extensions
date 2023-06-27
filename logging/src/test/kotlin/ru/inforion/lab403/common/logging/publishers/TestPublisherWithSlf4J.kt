package ru.inforion.lab403.common.logging.publishers

import org.slf4j.LoggerFactory
import ru.inforion.lab403.common.logging.logger.Record
import ru.inforion.lab403.common.logging.tests.SLF4JLoggerTest


class TestPublisherWithSlf4J : AbstractPublisher("TestPublisherWithSlf4J") {
    val log = LoggerFactory.getLogger(SLF4JLoggerTest::class.java);

    override fun publish(message: String, record: Record) {
        log.info(message)
    }

    override fun flush() = Unit
}