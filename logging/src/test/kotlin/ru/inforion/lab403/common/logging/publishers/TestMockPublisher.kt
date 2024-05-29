package ru.inforion.lab403.common.logging.publishers

import ru.inforion.lab403.common.logging.LogLevel
import ru.inforion.lab403.common.logging.logger.Logger
import java.util.*


class TestMockPublisher(name: String) : AbstractPublisher(name, false) {
    data class MockedMessage(
        val message: String,
        val level: LogLevel,
        val logger: Logger,
    );

    val messages = mutableListOf<MockedMessage>()

    fun removeFirst() = messages.removeFirst()
    val size get() = messages.size

    override fun publish(message: String, level: LogLevel, logger: Logger) {
        println(message)
        messages.add(MockedMessage(message, level, logger))
    }

    override fun flush() = Unit

    fun clear() {
        messages.clear()
    }
}

fun generateTestMockPublisher() =
    TestMockPublisher("TestMockPublisher " + UUID.randomUUID().toString())
