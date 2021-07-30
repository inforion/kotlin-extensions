@file:Suppress("NOTHING_TO_INLINE", "UNCHECKED_CAST", "unused")

package ru.inforion.lab403.common.reflection

import kotlin.reflect.KClass
import kotlin.reflect.KParameter

inline fun <T: Any> KParameter.kClass(): KClass<T> = type.classifier as KClass<T>

inline val KParameter.kClassAny get() = kClass<Any>()