@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.optional

import ru.inforion.lab403.common.extensions.unaryMinus

@PublishedApi internal val EMPTY: Optional<*> = Optional(Unit as Any)

@Suppress("UNCHECKED_CAST")
inline fun <T : Any> emptyOpt() = EMPTY as Optional<T>

inline val <T: Any> T?.opt get() = if (this != null) Optional(this) else emptyOpt()

inline infix fun <T: Any> Optional<T>.orElse(action: () -> T) = orNull ?: action()

@JvmName("unaryMinusULong")
inline operator fun Optional<ULong>.unaryMinus() = if (isPresent) Optional(-get) else emptyOpt()

@JvmName("unaryMinusUInt")
inline operator fun Optional<UInt>.unaryMinus() = if (isPresent) Optional(-get) else emptyOpt()