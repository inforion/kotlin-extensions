@file:Suppress("unused")

package ru.inforion.lab403.common.optional

import java.io.Serializable

class Optional<T: Any> constructor(@PublishedApi internal val data: T) : Serializable {
    inline val isPresent get() = data != Unit

    inline val isEmpty get() = data == Unit

    inline val get: T get() = checkNotNull(orNull) { "No value present" }

    inline val orNull: T? get() = data.takeIf { isPresent }

    override fun hashCode() = data.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Optional<*>) return false

        if (data != other.data) return false

        return true
    }

    override fun toString() = if (isPresent) "Optional($data)" else "Optional.empty"
}