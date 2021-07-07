@file:Suppress("NOTHING_TO_INLINE", "HasPlatformType", "unused")

package ru.inforion.lab403.common.javalin

import com.fasterxml.jackson.datatype.joda.JodaModule
import io.javalin.Javalin
import io.javalin.core.plugin.Plugin
import io.javalin.http.Context
import io.javalin.plugin.json.JavalinJackson
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

inline fun Context.errorResponse(status: Int, result: String) {
    status(status)
    result(result)  // body
}

// :E javalin didn't catch Throwable type exception and just set 500 error without logging!!!
inline fun <R> Context.tryWithErrorsHandle(action: () -> R) {
    try {
        action()
    } catch (error: Throwable) {
        error.printStackTrace()
        errorResponse(400, error.toString())
    }
}

typealias BlockAny = (ctx: Context) -> Any

typealias BlockBytes = (ctx: Context) -> ByteArray

typealias BlockVoid = (ctx: Context) -> Unit

inline fun Context.jsonSafe(block: BlockAny) = tryWithErrorsHandle { json(block(this)) }

inline fun Context.bytesSafe(block: BlockBytes) = tryWithErrorsHandle { result(block(this)) }

inline fun Context.voidSafe(block: BlockAny) = tryWithErrorsHandle {
    block(this)
    json(Unit)
}

inline fun Javalin.postAny(path: String, crossinline block: BlockAny) = post(path) { it.jsonSafe(block) }

inline fun Javalin.postVoid(path: String, crossinline block: BlockVoid) = post(path) { it.voidSafe(block) }

inline fun Javalin.deleteAny(path: String, crossinline block: BlockAny) = delete(path) { it.jsonSafe(block) }

inline fun Javalin.deleteVoid(path: String, crossinline block: BlockVoid) = delete(path) { it.voidSafe(block) }

inline fun Javalin.getAny(path: String, crossinline block: BlockAny) = get(path) { it.jsonSafe(block) }

inline fun Javalin.getBytes(path: String, crossinline block: BlockBytes) = get(path) { it.bytesSafe(block) }

inline fun Javalin.getAnyExclusive(
    path: String,
    lock: ReentrantLock,
    crossinline block: BlockAny
): Javalin = get(path) {
    it.tryWithErrorsHandle {
        lock.withLock { it.json(block(it)) }
    }
}



inline fun Javalin.applyRoutes(crossinline block: Javalin.() -> Unit) {
    routes {
        block(this)
    }
}

inline fun Javalin.stopAndWait() {
    val lock = LinkedBlockingQueue<Int>(1)
    events {
        it.serverStopped { lock.add(0) }
    }
    stop()
    lock.take()
}

fun JavalinServer(
    port: Int,
    vararg protocols: Plugin,
    start: Boolean = true,
    wait: Boolean = start
) = Javalin.create { config ->
    protocols.forEach { config.registerPlugin(it) }
}.apply {
    val lock = LinkedBlockingQueue<Int>(1)

    if (wait) events {
        it.serverStarted { lock.add(0) }
    }

    // register module to serialize JodaTime
    JavalinJackson.getObjectMapper().registerModule(JodaModule())

    if (start) start(port)

    if (wait) lock.take()
}