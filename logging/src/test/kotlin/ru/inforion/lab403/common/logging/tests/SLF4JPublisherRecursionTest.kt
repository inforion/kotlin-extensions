@file:Suppress("DEPRECATION")

package ru.inforion.lab403.common.logging.tests

import org.junit.Before
import org.junit.Test
import ru.inforion.lab403.common.logging.SEVERE
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.logging.config.LoggerConfig
import ru.inforion.lab403.common.logging.publishers.TestMockPublisher
import ru.inforion.lab403.common.logging.publishers.TestPublisherWithSlf4J
import java.util.logging.Level
import kotlin.test.assertEquals

internal class SLF4JPublisherRecursionTest {
    private lateinit var publisher: TestMockPublisher
    private lateinit var publisherWithSlf4J: TestPublisherWithSlf4J

    @Before
    fun initPublisher() {
        LoggerConfig.clearPublishers()
        publisher = TestMockPublisher().also {
            LoggerConfig.addPublisher(it)
        }
        publisherWithSlf4J = TestPublisherWithSlf4J().also {
            LoggerConfig.addPublisher(it, "*.SLF4JLoggerTest")
        }
    }

    @Test
    fun test1() {
        val log = logger(Level.FINE)

        log.severe { "First severe message..." }
        log.debug { "Not logged message" }
        assertEquals(1, publisher.size)
        publisher.removeFirst().also {
            assertEquals("First severe message...", it.message)
            assertEquals(SEVERE, it.record.level)
        }

        assertEquals(LoggerConfig.publishers(LoggerConfig.ALL).size, 2)
        assertEquals(LoggerConfig.publishers("*.SLF4JLoggerTest").size, 1)

        LoggerConfig.removePublisher(publisherWithSlf4J, "*.SLF4JLoggerTest")
        assertEquals(LoggerConfig.publishers("*.SLF4JLoggerTest").size, 0)
    }
}
