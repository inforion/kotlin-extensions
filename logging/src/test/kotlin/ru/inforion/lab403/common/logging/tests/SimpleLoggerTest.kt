package ru.inforion.lab403.common.logging.tests

import org.junit.Before
import org.junit.Test
import ru.inforion.lab403.common.logging.FINE
import ru.inforion.lab403.common.logging.SEVERE
import ru.inforion.lab403.common.logging.WARNING
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.logging.logger.Config
import ru.inforion.lab403.common.logging.logger.Logger
import ru.inforion.lab403.common.logging.publishers.TestMockPublisher
import kotlin.test.assertEquals


internal class SimpleLoggerTest {
    private lateinit var publisher: TestMockPublisher

    @Before
    fun initPublisher() {
        Config.clearPublishers()
        publisher = TestMockPublisher().also {
            Config.addPublisher(it)
        }
    }

    @Test
    fun test1() {
        val log = logger(FINE)

        log.warning { "First severe message..." }
        assertEquals(1, publisher.size)
        publisher.removeFirst().also {
            assertEquals("First severe message...", it.message)
            assertEquals(WARNING, it.record.level)
        }
    }
}