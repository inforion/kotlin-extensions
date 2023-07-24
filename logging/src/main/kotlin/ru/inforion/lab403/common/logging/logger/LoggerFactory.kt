package ru.inforion.lab403.common.logging.logger

import org.slf4j.ILoggerFactory

class LoggerFactory : ILoggerFactory {
    override fun getLogger(name: String?): org.slf4j.Logger {
        if (name == null)
            throw IllegalArgumentException("Expected logger name to be not null")
        return Slf4jLoggerImpl(name)
    }
}