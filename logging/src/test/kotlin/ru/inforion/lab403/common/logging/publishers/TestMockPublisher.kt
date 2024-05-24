package ru.inforion.lab403.common.logging.publishers

import ru.inforion.lab403.common.logging.logger.Record
import java.util.*


class TestMockPublisher(name: String) : AbstractPublisher((name)) {
    data class MockedMessage(
        val message: String,
        val record: Record,
    );

    val messages = mutableListOf<MockedMessage>()

    fun removeFirst() = messages.removeFirst()
    val size get() = messages.size

    override fun publish(message: String, record: Record) {
        println(message)
        messages.add(MockedMessage(message, record))
    }

    override fun flush() = Unit

    fun clear() {
        messages.clear()
    }
}

fun generateTestMockPublisher() =
    TestMockPublisher("TestMockPublisher " + UUID.randomUUID().toString())
