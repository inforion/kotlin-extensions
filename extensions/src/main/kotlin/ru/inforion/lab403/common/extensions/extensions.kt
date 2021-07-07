package ru.inforion.lab403.common.extensions

inline infix fun <T: Any, R> T?.ifNotNull(action: T.() -> R) = if (this != null) action(this) else null

inline infix fun <T: Any, R> T?.ifItNotNull(action: (T) -> R) = if (this != null) action(this) else null

inline infix fun <T: Any?> T?.either(action: () -> T) = this ?: action()

inline infix fun <T: Any?> T?.otherwise(action: () -> Unit): T? {
    if (this == null) action()
    return this
}