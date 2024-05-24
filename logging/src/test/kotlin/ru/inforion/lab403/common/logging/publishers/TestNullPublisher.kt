package ru.inforion.lab403.common.logging.publishers

import ru.inforion.lab403.common.logging.logger.Record
import java.util.UUID
import kotlin.random.Random


class TestNullPublisher(name: String) : AbstractPublisher(name) {
    override fun publish(message: String, record: Record) = Unit

    override fun flush() = Unit
}

fun generateTestNullPublisher() =
    TestNullPublisher("TestNullPublisher " + UUID.randomUUID().toString())