@file:Suppress("DEPRECATION")

package ru.inforion.lab403.common.logging.tests

import org.junit.Before
import org.junit.Test
import ru.inforion.lab403.common.logging.SEVERE
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.logging.storage.LoggerStorage
import ru.inforion.lab403.common.logging.publishers.TestMockPublisher
import ru.inforion.lab403.common.logging.publishers.TestPublisherWithSlf4J
import java.util.logging.Level
import kotlin.test.assertEquals

internal class SLF4JPublisherRecursionTest {
    private lateinit var publisher: TestMockPublisher
    private lateinit var publisherWithSlf4J: TestPublisherWithSlf4J

    @Before
    fun initPublisher() {
        LoggerStorage.clearPublishers()
        publisher = TestMockPublisher().also {
            LoggerStorage.addPublisher(it)
        }
        publisherWithSlf4J = TestPublisherWithSlf4J().also { // Из-за этого логгера не проходит первый assert
            LoggerStorage.addPublisher(it, this::class.java.name)
        }
    }

    @Test
    fun test1() {
        val log = logger(Level.FINE)

        LoggerStorage.getLoggerConfigurationsString()

        log.severe { "First severe message..." }
        log.debug { "Not logged message" }
        assertEquals(1, publisher.size)
        publisher.removeFirst().also {
            assertEquals("First severe message...", it.message)
            assertEquals(SEVERE, it.record.level)
        }

        assertEquals(LoggerStorage.publishers(LoggerStorage.ALL).size, 2)
        assertEquals(LoggerStorage.publishers(this::class.java.name).size, 3)

        LoggerStorage.removePublisher(publisherWithSlf4J, this::class.java.name)
        assertEquals(LoggerStorage.publishers(this::class.java.name).size, 2)
    }
}
