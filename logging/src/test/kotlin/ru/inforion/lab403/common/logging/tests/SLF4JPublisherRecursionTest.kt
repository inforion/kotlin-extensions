@file:Suppress("DEPRECATION")

package ru.inforion.lab403.common.logging.tests

import org.junit.Before
import org.junit.Test
import ru.inforion.lab403.common.logging.SEVERE
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.logging.logger.Config
import ru.inforion.lab403.common.logging.publishers.TestMockPublisher
import ru.inforion.lab403.common.logging.publishers.TestPublisherWithSlf4J
import java.util.logging.Level
import kotlin.test.assertEquals

internal class SLF4JPublisherRecursionTest {
    private lateinit var publisher: TestMockPublisher
    private lateinit var publisherWithSlf4J: TestPublisherWithSlf4J

    @Before
    fun initPublisher() {
        Config.clearPublishers()
        publisher = TestMockPublisher().also {
            Config.addPublisher(it)
        }
        publisherWithSlf4J = TestPublisherWithSlf4J().also {
            Config.addPublisher(it, "*.SLF4JLoggerTest")
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

        assertEquals(Config.publishers(Config.ALL).size, 2)
        assertEquals(Config.publishers("*.SLF4JLoggerTest").size, 1)

        Config.removePublisher(publisherWithSlf4J, "*.SLF4JLoggerTest")
        assertEquals(Config.publishers("*.SLF4JLoggerTest").size, 0)
    }
}
