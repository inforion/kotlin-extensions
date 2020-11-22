@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions

/**
 * Function works like Kotlin org.jetbrains.kotlin.utils.sure from kotlin-compiler-embeddable
 *
 * If [this] is not null then returns [this] else throws exception with specified message
 *
 * @since 0.3.4
 */
inline fun <T: Any> T?.sure(message: () -> String) = this ?: throw IllegalArgumentException(message())