@file:Suppress("DEPRECATION")

package ru.inforion.lab403.common.logging.tests

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.inforion.lab403.common.logging.SEVERE
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.logging.storage.LoggerStorage
import ru.inforion.lab403.common.logging.publishers.TestMockPublisher
import java.util.logging.Level
import kotlin.test.assertEquals

internal class CompatLoggerTest {
    private lateinit var publisher: TestMockPublisher

    @BeforeEach
    fun initPublisher() {
        LoggerStorage.clearPublishers()
        publisher = TestMockPublisher("TestMockPublisher").also {
            LoggerStorage.addPublisher(LoggerStorage.ALL, it)
        }
    }

    @Test
    fun test1() {
        val log = logger(Level.FINE)

        log.severe { "First severe message..." }
        assertEquals(1, publisher.size)
        publisher.removeFirst().also {
            assertEquals("First severe message...", it.message)
            assertEquals(SEVERE, it.level)
        }
    }
}