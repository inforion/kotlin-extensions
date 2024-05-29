package ru.inforion.lab403.common.logging.tests

import org.junit.Before
import org.junit.Test
import ru.inforion.lab403.common.logging.*
import ru.inforion.lab403.common.logging.logger.Logger
import ru.inforion.lab403.common.logging.storage.LoggerStorage
import ru.inforion.lab403.common.logging.publishers.TestMockPublisher
import ru.inforion.lab403.common.logging.publishers.TestNullPublisher
import ru.inforion.lab403.common.logging.publishers.setupPublishersTestNull
import kotlin.test.assertEquals


internal class SimpleLoggerTest {
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
    fun test1() {
        val log = logger(FINE)

        log.warning { "First severe message..." }
        assertEquals(1, publisher.size)
        publisher.removeFirst().also {
            assertEquals("First severe message...", it.message)
            assertEquals(WARNING, it.record.level)
        }
    }

    @Test
    fun testCustomName() {
        val log = Logger.create("a.b.c.d.e.f", false)

        LoggerStorage.addPublisher(LoggerStorage.ALL,
            TestNullPublisher("TestNullPublisher-1")
        )
        LoggerStorage.addPublisher("a.b.c",
            TestNullPublisher("TestNullPublisher-2")
        )
        LoggerStorage.addPublisher("a",
            TestNullPublisher("TestNullPublisher-3")
        )
        LoggerStorage.addPublisher("a.b.c.d.e",
            TestNullPublisher("TestNullPublisher-4")
        )

        LoggerStorage.setAdditivity("a.b.c", false)

        log.severe { "Test" }
    }

    @Test
    fun testLevelling() {
        val logFine = Logger.create("a.b.c.d.e.f", false)
        val logInfo = Logger.create("a.b.c.d.e", false)
        val logSevere = Logger.create("a.b.c", false)

        LoggerStorage.setLevel(".a.b.c", SEVERE)
        LoggerStorage.setLevel(".a.b.c.d.e", INFO)
        LoggerStorage.setLevel(".a.b.c.d.e.f", FINE)

        assertEquals(publisher.size, 0)

        logSevere.severe { "Test logSevere severe" }
        logSevere.info { "Test logSevere info" }
        logSevere.fine { "Test logSevere fine" }
        assertEquals(publisher.size, 1)
        assertEquals(publisher.messages[0].message, "Test logSevere severe")
        publisher.clear()

        logInfo.severe { "Test logInfo severe" }
        logInfo.info { "Test logInfo info" }
        logInfo.fine { "Test logInfo fine" }
        assertEquals(publisher.size, 2)
        assertEquals(publisher.messages[0].message, "Test logInfo severe")
        assertEquals(publisher.messages[1].message, "Test logInfo info")
        publisher.clear()

        logFine.severe { "Test logFine severe" }
        logFine.info { "Test logFine info" }
        logFine.fine { "Test logFine fine" }
        assertEquals(publisher.size, 3)
        assertEquals(publisher.messages[0].message, "Test logFine severe")
        assertEquals(publisher.messages[1].message, "Test logFine info")
        assertEquals(publisher.messages[2].message, "Test logFine fine")
    }

    @Test
    fun jmhTest() {
        setupPublishersTestNull()

        val loggers = listOf(
            Logger.create("a.b.c.d.e.f", false),
            Logger.create("a.b.c.d.h.i", false),
            Logger.create("a.b.c.x", false),
            Logger.create("a.y", false),
        )

        loggers.forEach {
            it.info {
                "My message 1"
            }
        }
    }
}