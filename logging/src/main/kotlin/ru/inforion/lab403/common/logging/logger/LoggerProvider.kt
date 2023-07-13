package ru.inforion.lab403.common.logging.logger

import org.slf4j.ILoggerFactory
import org.slf4j.IMarkerFactory
import org.slf4j.helpers.BasicMDCAdapter
import org.slf4j.spi.MDCAdapter
import org.slf4j.spi.SLF4JServiceProvider
import org.slf4j.helpers.BasicMarkerFactory

class LoggerProvider : SLF4JServiceProvider {
    override fun getLoggerFactory(): ILoggerFactory {
        return LoggerFactory()
    }

    override fun getMarkerFactory(): IMarkerFactory = BasicMarkerFactory()
    override fun getMDCAdapter(): MDCAdapter = BasicMDCAdapter()

    override fun getRequestedApiVersion(): String {
        return VERSION
    }

    override fun initialize() {
    }

    companion object {
        private const val VERSION = "2.0.6"
    }
}
