@file:Suppress("DEPRECATION")

package ru.inforion.lab403.common.logging.tests

import org.junit.Before
import org.junit.Test
import ru.inforion.lab403.common.logging.SEVERE
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.logging.logger.Logger
import ru.inforion.lab403.common.logging.publishers.TestMockPublisher
import java.util.logging.Level
import kotlin.test.assertEquals

internal class CompatLoggerTest {
    private val publisher = TestMockPublisher().also {
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