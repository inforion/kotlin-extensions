package ru.inforion.lab403.common.logging.tests

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import ru.inforion.lab403.common.logging.DEBUG
import ru.inforion.lab403.common.logging.INFO
import ru.inforion.lab403.common.logging.WARNING
import ru.inforion.lab403.common.logging.storage.LoggerStorage
import ru.inforion.lab403.common.logging.publishers.TestMockPublisher
import ru.inforion.lab403.common.logging.publishers.generateTestMockPublisher
import kotlin.test.assertEquals

class SLF4JLoggerTest {
    private lateinit var publisher: TestMockPublisher

    @BeforeEach
    fun initPublisher() {
        LoggerStorage.clearPublishers()
        publisher = generateTestMockPublisher().also {
            LoggerStorage.addPublisher(LoggerStorage.ALL, it)
        }
    }

    @Test
    fun testFormatString() {
        val log = LoggerFactory.getLogger(SLF4JLoggerTest::class.java)

        log.warn("message {} with {} args: {}", 1, "different", 3)
        log.info("message {} with {} args: {}", 3, "another", 4)

        assertEquals(2, publisher.size)
        publisher.removeFirst().also {
            assertEquals("message 1 with different args: 3", it.message)
            assertEquals(WARNING, it.level)
        }
        publisher.removeFirst().also {
            assertEquals("message 3 with another args: 4", it.message)
            assertEquals(INFO, it.level)
        }
    }

    @Test
    fun testDebugFormatCrash() {
        val log = LoggerFactory.getLogger(SLF4JLoggerTest::class.java)
        LoggerStorage.setLevel(LoggerStorage.ALL, level = DEBUG)

        println(LoggerStorage.getLoggerConfigurationsString())

        log.debug("message {} with {} args: {}", 1, "different", 3)
        val secondMessage =
            "filterNameMap={org.apache.spark.ui.HttpSecurityFilter-7978e022=org.apache.spark.ui.HttpSecurityFilter-7978e022==org.apache.spark.ui.HttpSecurityFilter@7978e022{inst=false,async=true,src=EMBEDDED:null}}"
        log.debug(secondMessage, *emptyList<String>().toTypedArray())

        assertEquals(2, publisher.size)
        publisher.removeFirst().also {
            assertEquals("message 1 with different args: 3", it.message)
            assertEquals(DEBUG, it.level)
        }
        publisher.removeFirst().also {
            assertEquals(secondMessage, it.message)
            assertEquals(DEBUG, it.level)
        }
    }
}
