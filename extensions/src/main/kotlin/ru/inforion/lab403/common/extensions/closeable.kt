@file:Suppress("unused")

package ru.inforion.lab403.common.extensions

import java.io.Closeable

class ResourceManager : Iterable<Closeable> {
    val context = mutableListOf<Closeable>()

    inline fun <T : Closeable?> add(closeable: () -> T): T {
        val value = closeable()
        if (value != null) context += value
        return value
    }

    operator fun <T : Closeable?> T.unaryPlus() = add { this }

    operator fun <T : Closeable?> plus(closeable: T) = add { closeable }

    inline operator fun invoke(action: ResourceManager.() -> Unit) {
        try {
            action()
        } finally {
            close()
        }
    }

    fun close() {
        var exception: Throwable? = null

        context
            .asReversed()
            .onEach {
                it.runCatching { close() }
                    .onFailure { error: Throwable -> exception = error }
            }.clear()

        if (exception != null) throw exception as Throwable
    }

    override fun iterator() = context.iterator()
}

inline fun scope(
    manager: ResourceManager = ResourceManager(),
    action: ResourceManager.() -> Unit
) = manager.invoke(action)