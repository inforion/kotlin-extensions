@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.concurrent

import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun <T> async(
    context: CoroutineContext = EmptyCoroutineContext,
    scope: CoroutineScope = GlobalScope,
    block: suspend CoroutineScope.() -> T
) = scope.async(context, block = block)

fun launch(
    context: CoroutineContext = EmptyCoroutineContext,
    scope: CoroutineScope = GlobalScope,
    block: suspend CoroutineScope.() -> Unit
) = scope.launch(context, block = block)

fun <T> Collection<Deferred<T>>.wait() = runBlocking { awaitAll() }

fun Collection<Job>.wait() = runBlocking { joinAll() }

/**
 * Waits until all jobs in [this] collection not finished and then clear collection
 *
 * @since 0.3.4
 */
fun MutableCollection<Job>.waitAndClear() {
    wait()
    clear()
}

fun <T> Deferred<T>.wait() = runBlocking { await() }

fun Job.wait() = runBlocking { join() }

/**
 * Executes an [action] for [this] deferred when completed with it result
 *
 * @since 0.3.4
 */
inline fun <T> Deferred<T>.onComplete(crossinline action: (T) -> Unit) = invokeOnCompletion { action(getCompleted()) }

/**
 * Represent blocking value class, like BlockingQueue but just with one element
 *
 * @since 0.3.3
 */
class BlockingValue <T: Any> {
    private lateinit var value: T

    private val lock = ReentrantLock()
    private val condition = lock.newCondition()

    private var hasValue = false

    private fun setWithSignal(newValue: T) {
        value = newValue
        hasValue = true
        condition.signal()
    }

    private fun valueOrNull() = if (hasValue) value else null

    fun hasValue() = lock.withLock { hasValue }

    /**
     * Try to get value without waiting, returns false if value already set else true
     *
     * i.e. NON-BLOCKING get-method
     *
     * @param newValue value to set
     */
    fun offer(newValue: T) = lock.withLock {
        if (hasValue) false else {
            setWithSignal(newValue)
            true
        }
    }

    /**
     * Try to get value infinitely
     *
     * i.e. BLOCKING get-method
     */
    fun take() = lock.withLock {
        if (!hasValue) condition.await()
        value
    }

    /**
     * Try to get value during specified timeout
     *
     * i.e. TIMEOUT-BLOCKING get-method
     *
     * @param millis timeout in milliseconds
     */
    fun poll(millis: Long) = lock.withLock {
        if (!hasValue) {
            condition.await(millis, TimeUnit.MILLISECONDS)
            valueOrNull()
        } else value
    }

    /**
     * Try to get value without waiting, returns null if value not set
     *
     * i.e. NON-BLOCKING get-method
     */
    fun poll() = lock.withLock { valueOrNull() }
}