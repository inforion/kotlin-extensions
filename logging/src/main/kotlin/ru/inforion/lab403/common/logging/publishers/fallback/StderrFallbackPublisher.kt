package ru.inforion.lab403.common.logging.publishers.fallback

class StderrFallbackPublisher : AbstractFallbackPublisher() {
    override fun publish(message: String) {
        System.err.println(message)
    }
}