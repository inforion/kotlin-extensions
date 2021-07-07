@file:Suppress("unused")

package ru.inforion.lab403.common.concurrent.events

import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

open class ConditionEventFuture(private val event: Event, val time: Long = 1000) : Future<Boolean> {
    private var signaled = false
    private var canceled = false
    private var exception = false

    open fun condition() = true

    override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
        canceled = true
        return true
    }

    override fun isCancelled() = canceled

    override fun isDone() = signaled || canceled || exception

    private fun checkCondition() {
        if (!condition()) {
            exception = true
            throw RuntimeException("Working condition for the Future doesn't met")
        }
    }

    override fun get(): Boolean {
        while (!event.await(time, TimeUnit.MILLISECONDS))
            checkCondition()
        signaled = true
        return true
    }

    override fun get(timeout: Long, unit: TimeUnit): Boolean {
        val timeInUnit = unit.convert(time, TimeUnit.MILLISECONDS)
        var remain = timeout

        while (remain != 0L) {
            val toWait = timeInUnit.coerceAtMost(remain)
            if (event.await(toWait, unit)) {
                signaled = true
                return true
            }
            checkCondition()
            remain -= toWait
        }

        return false
    }
}