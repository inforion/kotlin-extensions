package ru.inforion.lab403.common.logging.tests

import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory
import ru.inforion.lab403.common.extensions.getResourceText
import ru.inforion.lab403.common.logging.INFO
import ru.inforion.lab403.common.logging.WARNING
import ru.inforion.lab403.common.logging.logger.Logger
import ru.inforion.lab403.common.logging.publishers.TestMockPublisher
import kotlin.test.assertEquals

class SLF4JLoggerTest {
    private val publisher = TestMockPublisher().also {
        Logger.addPublisher(it)
    }

    @Before
    fun initPublisher() {
        publisher.clear()
    }

    @Test
    fun testFormatString() {
        val log = LoggerFactory.getLogger(SLF4JLoggerTest::class.java);

        log.warn("message {} with {} args: {}", 1, "different", 3)
        log.info("message {} with {} args: {}", 3, "another", 4)

        assertEquals(2, publisher.size)
        publisher.removeFirst().also {
            assertEquals("message 1 with different args: 3", it.message)
            assertEquals(WARNING, it.record.level)
        }
        publisher.removeFirst().also {
            assertEquals("message 3 with another args: 4", it.message)
            assertEquals(INFO, it.record.level)
        }
    }
}
