@file:Suppress("unused")

package ru.inforion.lab403.common.concurrent.notifications

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Notification {
    private val lock = ReentrantLock()

    private val notify = lock.newCondition()
    private val subscribed = lock.newCondition()
    private val ready = lock.newCondition()

    private val subscribers = mutableListOf<Subscriber>()

    inner class Subscriber {
        var registered = true
            private set

        var isReady = false
            internal set

        fun await() {
            check(registered) { "Can't await on unregistered Subscriber" }
            locked {
                isReady = true
                ready.signalAll()
                notify.await()
            }
        }

        fun unregister() = locked {
            registered = false
            subscribers.remove(this)
            subscribed.signalAll()
            ready.signalAll()  // also signal on ready cus we unsubscribe and so notifier may not wait for us
        }
    }

    private inline fun <R>locked(action: () -> R): R = lock.withLock(action)

    private fun doWaitForSubscribers(count: Int) {
        while (subscribers.size < count)
            subscribed.await()
    }

    private fun doWaitSubscribersReady() {
        while (!subscribers.all { it.isReady })
            ready.await()
    }

    private fun doNotifySubscribers() {
        notify.signalAll()
        subscribers.forEach { it.isReady = false }
    }

    fun newSubscriber() = locked {
        Subscriber().also {
            subscribers.add(it)
            subscribed.signalAll()
        }
    }

    fun waitForSubscribers(count: Int = 1) = locked { doWaitForSubscribers(count) }

    fun notifySubscribers() = locked { doNotifySubscribers() }

    fun waitSubscribersReady() = locked { doWaitSubscribersReady() }

    fun notifyAndWaitSubscribersReady() = locked {
        doNotifySubscribers()
        doWaitSubscribersReady()
    }
}