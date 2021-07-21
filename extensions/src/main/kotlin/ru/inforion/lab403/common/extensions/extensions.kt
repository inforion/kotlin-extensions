package ru.inforion.lab403.common.extensions

inline infix fun <T: Any, R> T?.ifNotNull(action: T.() -> R) = if (this != null) action(this) else null

inline infix fun <T: Any, R> T?.ifItNotNull(action: (T) -> R) = if (this != null) action(this) else null

inline infix fun <T: Any?> T?.either(action: () -> T) = this ?: action()

inline infix fun <T: Any?> T?.otherwise(action: () -> Unit): T? {
    if (this == null) action()
    return this
}

/**
 * Function works like Kotlin org.jetbrains.kotlin.utils.sure from kotlin-compiler-embeddable
 *
 * If [this] is not null then returns [this] else throws exception with specified message
 *
 * @since 0.3.4
 */
inline fun <T: Any> T?.sure(message: () -> String) = this ?: throw IllegalArgumentException(message())