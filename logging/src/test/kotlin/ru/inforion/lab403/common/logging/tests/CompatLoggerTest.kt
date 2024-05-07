@file:Suppress("DEPRECATION")

package ru.inforion.lab403.common.logging.tests

import org.junit.Before
import org.junit.Test
import ru.inforion.lab403.common.logging.SEVERE
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.logging.storage.LoggerStorage
import ru.inforion.lab403.common.logging.publishers.TestMockPublisher
import java.util.logging.Level
import kotlin.test.assertEquals

internal class CompatLoggerTest {
    private lateinit var publisher: TestMockPublisher

    @Before
    fun initPublisher() {
        LoggerStorage.clearPublishers()
        publisher = TestMockPublisher().also {
            LoggerStorage.addPublisher(it)
        }
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