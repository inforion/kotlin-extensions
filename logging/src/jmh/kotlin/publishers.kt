package ru.inforion.lab403.common.logging.jmh

import ru.inforion.lab403.common.logging.logger.Record
import ru.inforion.lab403.common.logging.publishers.AbstractPublisher

class TestPublisher(name: String): AbstractPublisher(name) {
    override fun flush() = Unit

    override fun publish(message: String, record: Record) {
        println("TestPublisher says $message")
    }
}

class TestPublisherSecond(name: String): AbstractPublisher(name) {
    override fun flush() = Unit

    override fun publish(message: String, record: Record) {
        println("TestPublisherSecond says $message")
    }
}
