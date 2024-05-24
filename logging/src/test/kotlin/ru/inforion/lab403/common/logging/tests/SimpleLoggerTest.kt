package ru.inforion.lab403.common.logging.tests

import org.junit.Before
import org.junit.Test
import ru.inforion.lab403.common.logging.FINE
import ru.inforion.lab403.common.logging.WARNING
import ru.inforion.lab403.common.logging.logger
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