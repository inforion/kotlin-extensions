@file:Suppress("DEPRECATION")

package ru.inforion.lab403.common.logging.tests

import org.junit.Before
import org.junit.Test
import ru.inforion.lab403.common.logging.SEVERE
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.logging.logger.Logger
import ru.inforion.lab403.common.logging.publishers.TestMockPublisher
import ru.inforion.lab403.common.logging.publishers.TestPublisherWithSlf4J
import java.util.logging.Level
import kotlin.test.assertEquals

internal class SLF4JPublisherRecursionTest {
    private val publisher = TestMockPublisher().also {
        Logger.addPublisher(it)
    }
    private val publisherWithSlf4J = TestPublisherWithSlf4J().also {
        Logger.addPublisher(it)
    }

    @Before
    fun initPublisher() {
        publisher.clear()
    }

    @Test
    fun test1() {
        val log = logger(Level.FINE)

        log.severe { "First severe message..." }
        assertEquals(1, publisher.size)
        publisher.removeFirst().also {
            assertEquals("First severe message...", it.message)
            assertEquals(SEVERE, it.record.level)
        }
    }
}
