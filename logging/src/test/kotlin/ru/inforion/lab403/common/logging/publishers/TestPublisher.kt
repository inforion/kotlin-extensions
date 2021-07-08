package ru.inforion.lab403.common.logging.publishers

import ru.inforion.lab403.common.logging.logger.Record


class TestPublisher(private val value0: Int, private val value1: String) : AbstractPublisher("TestPublisher") {
    override fun publish(message: String, record: Record) {
        println("message: $message, value0: $value0, value1: $value1, level: ${record.level}")
    }

    override fun flush() = Unit
}