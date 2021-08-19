package ru.inforion.lab403.common.optional

import java.io.Serializable

class Optional<T: Any> private constructor(@PublishedApi internal val data: T) : Serializable {
    companion object {
        private val EMPTY: Optional<*> = Optional(Unit as Any)

        fun <T: Any> of(value: T) = Optional(value)

        @Suppress("UNCHECKED_CAST")
        fun <T: Any> empty() = EMPTY as Optional<T>
    }

    inline val isPresent get() = data != Unit

    inline val isEmpty get() = data == Unit

    inline val valueOrNull get() = data.takeIf { isPresent }

    inline val value get() = check(isPresent) { "No value present" }

    inline fun orElse(action: () -> T) = valueOrNull ?: action()

    override fun hashCode() = data.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Optional<*>) return false

        if (data != other.data) return false

        return true
    }

    override fun toString() = if (isPresent) "Optional($data)" else "Optional.empty"
}