package ru.inforion.lab403.common.logging.jmh

import org.openjdk.jmh.infra.Blackhole
import ru.inforion.lab403.common.extensions.long_z
import ru.inforion.lab403.common.logging.logger.Record
import ru.inforion.lab403.common.logging.publishers.AbstractPublisher
import ru.inforion.lab403.common.logging.publishers.setupPublishers

class JmhPublisher(name: String, val blackhole: Blackhole) : AbstractPublisher(name) {
    override fun flush() = blackhole.consume(1)

    override fun publish(message: String, record: Record) {
        blackhole.consume(message)
    }
}

// TODO: мб можно куда-то унести, чтобы нормально было
fun setupPublishersJmh(blackhole: Blackhole) = setupPublishers { JmhPublisher(it, blackhole) }