package ru.inforion.lab403.common.logging.examples

import ru.inforion.lab403.common.logging.TRACE
import ru.inforion.lab403.common.logging.formatters.NotInformative
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.logging.publishers.StdoutBeautyPublisher
import ru.inforion.lab403.common.logging.storage.LoggerStorage

object Application {
    val log = logger(TRACE)


    @JvmStatic
    fun main(args: Array<String>) {
//        val publisher = object : AbstractPublisher("MyPublisher") {
//            override fun flush() = Unit
//
//            override fun publish(message: String, record: Record) {
//                println("${record.logger.name} -> $message")
//            }
//        }
//
//        LoggerConfig.addPublisher(publisher)

        //LoggerFileConfigInitializer().load()

        LoggerStorage.removePublisher(LoggerStorage.ALL)

        LoggerStorage.addPublisher(
            LoggerStorage.ALL,
            StdoutBeautyPublisher(formatter = NotInformative())
        )

        log.severe { "This is severe message" }
        log.warning { "This is warning message" }
        log.info { "This is info message" }
        log.config { "This is config message" }
        log.fine { "This is fine message" }
        log.finer { "This is finer message" }
        log.finest { "This is finest message" }
        log.debug { "This is debug message" }
        log.trace { "This is trace message" }
    }
}