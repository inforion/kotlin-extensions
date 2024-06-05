package ru.inforion.lab403.common.logging.tests

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import ru.inforion.lab403.common.logging.TRACE
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.logging.publishers.PrintStreamBeautyPublisher
import ru.inforion.lab403.common.logging.storage.LoggerStorage
import kotlin.test.assertEquals

class ThePublisher(name: String): PrintStreamBeautyPublisher(name, System.out, formatter = {
    message, level, logger -> "$message | $level | $logger"
}) {
    var counter = 0

    val log = logger()

    override fun publishPrepared(message: String) {
        super.publishPrepared(message)
        log.severe { "Printed message!" }
        counter += 1
    }
}

class PublisherRecursionTest {
    val publisher = ThePublisher("Testing")
    @BeforeEach
    fun initPublisher() {
        LoggerStorage.clearPublishers()
        LoggerStorage.addPublisher(LoggerStorage.ALL, publisher)
        LoggerStorage.setLevel(LoggerStorage.ALL, TRACE)
    }

    @Test
    fun test() {
        val log = logger()
        log.debug { "Test string 1" }
        log.info { "Test string 2" }
        log.severe { "Test string 3" }

        assertEquals(3, publisher.counter)
    }
}
