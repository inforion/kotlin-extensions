package ru.inforion.lab403.common.logging.tests

import org.junit.Before
import org.junit.Test
import ru.inforion.lab403.common.logging.FINE
import ru.inforion.lab403.common.logging.INFO
import ru.inforion.lab403.common.logging.SEVERE
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.logging.publishers.TestMockPublisher
import ru.inforion.lab403.common.logging.storage.LoggerStorage
import kotlin.test.assertEquals

internal class LoggerCacheControlTest {
    private lateinit var publisher: TestMockPublisher

    @Before
    fun initPublisher() {
        LoggerStorage.clearPublishers()
        publisher = TestMockPublisher("TestMockPublisher").also {
            LoggerStorage.addPublisher(LoggerStorage.ALL, it)
        }
        LoggerStorage.clearLoggers()

        LoggerStorage.setLevel(LoggerStorage.ALL, INFO)
    }

    @Test
    fun testCacheInvalidation(){
        val log = logger(FINE)

        assertEquals(false, log.hasCache())
        log.fine { "Fine message" }
        assertEquals(true, log.hasCache())

        LoggerStorage.setLevel(".ru.inforion.lab403.common.logging.tests.LoggerCacheControlTest", INFO)

        assertEquals(false, log.hasCache())
        log.info { "Info message" }
        assertEquals(true, log.hasCache())

        LoggerStorage.setLevel(".inforion.lab403.common.logging", SEVERE)

        assertEquals(true, log.hasCache())

        LoggerStorage.setLevel(".ru.inforion.lab403.common.logging", SEVERE)

        assertEquals(false, log.hasCache())
        log.info { "Info message" }
        assertEquals(true, log.hasCache())
    }

}
