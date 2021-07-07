@file:Suppress("unused")

package ru.inforion.lab403.common.concurrent.events

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class StatefulEvent : Event {
    private val lock = ReentrantLock()
    private val notify = lock.newCondition()

    var isSet = false
        private set

    override fun await() = lock.withLock {
        if (!isSet) notify.await()
    }

    override fun await(time: Long, unit: TimeUnit) = lock.withLock {
        if (!isSet) notify.await(time, unit) else true
    }

    fun awaitAndClear() = lock.withLock {
        if (!isSet) notify.await()
        isSet = false
    }

    fun awaitAndClear(time: Long, unit: TimeUnit): Boolean = lock.withLock {
        if (isSet || notify.await(time, unit)) {
            isSet = false
            return@withLock true
        }
        return@withLock false
    }

    override fun signal() = lock.withLock {
        isSet = true
        notify.signalAll()
    }
}