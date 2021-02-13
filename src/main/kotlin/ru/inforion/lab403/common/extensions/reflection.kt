@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions

import kotlin.reflect.KClass
import kotlin.reflect.KParameter

@Suppress("UNCHECKED_CAST")
inline fun <T: Any>KParameter.kClass(): KClass<T> = type.classifier as KClass<T>

inline val KParameter.kClassAny get() = kClass<Any>()
