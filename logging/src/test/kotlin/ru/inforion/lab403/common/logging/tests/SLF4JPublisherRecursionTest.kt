@file:Suppress("DEPRECATION")

package ru.inforion.lab403.common.logging.tests

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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

    @BeforeEach
    fun initPublisher() {
        LoggerStorage.clearPublishers()
        publisher = TestMockPublisher("TestMockPublisher").also {
            LoggerStorage.addPublisher(LoggerStorage.ALL, it)
        }
        publisherWithSlf4J = TestPublisherWithSlf4J().also { // Из-за этого логгера не проходит первый assert
            LoggerStorage.addPublisher(".${this::class.java.name}", it)
        }
    }

    @Test
    fun test1() {
        val log = logger(Level.FINE)

        println(LoggerStorage.getLoggerConfigurationsString())

        log.severe { "First severe message..." }
        log.debug { "Not logged message" }
        assertEquals(1, publisher.size)
        publisher.removeFirst().also {
            assertEquals("First severe message...", it.message)
            assertEquals(SEVERE, it.level)
        }

        assertEquals(LoggerStorage.collectPublishers(LoggerStorage.ALL).size, 2)
        assertEquals(LoggerStorage.collectPublishers(".${this::class.java.name}").size, 3)

        LoggerStorage.removePublisher(".${this::class.java.name}", publisherWithSlf4J)
        assertEquals(LoggerStorage.collectPublishers(".${this::class.java.name}").size, 2)
    }
}
