package ru.inforion.lab403.common.logging.publishers

import ru.inforion.lab403.common.logging.LogLevel
import ru.inforion.lab403.common.logging.logger.Logger
import ru.inforion.lab403.common.logging.storage.LoggerStorage

abstract class AbstractPublisher(
    val name: String,
    val flushEnabled: Boolean,
) {
    companion object {
        private var mutex: Boolean = false
    }

    open fun prepareAndPublish(message: String, level: LogLevel, logger: Logger) {
        if (!mutex) {
            mutex = true
            try {
                publish(message, level, logger)
            } catch (e: Exception) {
                LoggerStorage.fallbackPublishers.forEach {
                    // TODO: maybe encapsulate
                    it.publish("Publisher [$name] failed to publish message '$message' with error: $e")
                }
            } finally {
                mutex = false
            }
        }
    }

    abstract fun publish(message: String, level: LogLevel, logger: Logger)

    fun checkAndFlush() {
        if (flushEnabled) {
            flush()
        }
    }

    abstract fun flush()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AbstractPublisher

        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}