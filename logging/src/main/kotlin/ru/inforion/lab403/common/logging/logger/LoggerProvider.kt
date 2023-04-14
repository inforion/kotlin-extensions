package ru.inforion.lab403.common.logging.logger

import org.slf4j.ILoggerFactory
import org.slf4j.IMarkerFactory
import org.slf4j.spi.MDCAdapter
import org.slf4j.spi.SLF4JServiceProvider

class LoggerProvider: SLF4JServiceProvider {

    override fun getLoggerFactory(): ILoggerFactory {
        return LoggerFactory()
    }

    override fun getMarkerFactory(): IMarkerFactory {
        throw NotImplementedError("Marker logging not implemented")
    }

    override fun getMDCAdapter(): MDCAdapter {
        throw NotImplementedError("MDCA adapter not implemented")
    }

    override fun getRequestedApiVersion(): String {
        return VERSION
    }

    override fun initialize() {
    }

    companion object {
        private const val VERSION = "2.0.6"
    }
}
