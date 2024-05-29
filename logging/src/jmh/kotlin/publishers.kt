package ru.inforion.lab403.common.logging.jmh

import org.openjdk.jmh.infra.Blackhole
import ru.inforion.lab403.common.logging.LogLevel
import ru.inforion.lab403.common.logging.formatters.NotInformative
import ru.inforion.lab403.common.logging.logger.Logger
import ru.inforion.lab403.common.logging.publishers.AbstractBeautyPublisher
import ru.inforion.lab403.common.logging.publishers.AbstractPublisher
import ru.inforion.lab403.common.logging.publishers.setupPublishers

class JmhPublisher(name: String, val blackhole: Blackhole) : AbstractPublisher(name, false) {
    override fun flush() = blackhole.consume(1)

    override fun publish(message: String, level: LogLevel, logger: Logger) {
        blackhole.consume(message)
//        println("JmhPublisher: $name $message")
    }
}

class JmhInformativePublisher(name: String, val blackhole: Blackhole) :
    AbstractBeautyPublisher(name, false, NotInformative(null, null, false)) {
    override fun flush() = blackhole.consume(1)

    override fun publishPrepared(message: String) {
        blackhole.consume(message)
//        println("JmhInformativePublisher: $name $message")
    }
}

fun setupPublishersJmh(blackhole: Blackhole) = setupPublishers { JmhPublisher(it, blackhole) }