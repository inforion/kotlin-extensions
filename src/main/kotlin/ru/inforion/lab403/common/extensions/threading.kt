package ru.inforion.lab403.common.extensions

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun <T> async(
    context: CoroutineContext = EmptyCoroutineContext,
    scope: CoroutineScope = GlobalScope,
    block: suspend CoroutineScope.() -> T
) = scope.async (context, block = block)

fun launch(
    context: CoroutineContext = EmptyCoroutineContext,
    scope: CoroutineScope = GlobalScope,
    block: suspend CoroutineScope.() -> Unit
) = scope.launch(context, block = block)

fun <T> Collection<Deferred<T>>.wait() = runBlocking { awaitAll() }

fun Collection<Job>.wait() = runBlocking { joinAll() }

fun <T> Deferred<T>.wait() = runBlocking { await() }

fun Job.wait() = runBlocking { join() }