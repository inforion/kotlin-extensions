package ru.inforion.lab403.common.extensions.concurrent.events

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class SimpleEvent : Event {
    private val lock = ReentrantLock()
    private val condition = lock.newCondition()

    override fun await() = lock.withLock { condition.await() }

    override fun await(time: Long, unit: TimeUnit) = lock.withLock { condition.await(time, unit) }

    override fun signal() = lock.withLock { condition.signalAll() }
}