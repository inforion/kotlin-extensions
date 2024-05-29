package ru.inforion.lab403.common.logging.publishers.fallback

abstract class AbstractFallbackPublisher {
    abstract fun publish(message: String)
}