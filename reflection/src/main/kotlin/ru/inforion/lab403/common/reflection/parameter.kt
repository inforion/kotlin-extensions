@file:Suppress("NOTHING_TO_INLINE", "UNCHECKED_CAST", "unused")

package ru.inforion.lab403.common.reflection

import kotlin.reflect.KClass
import kotlin.reflect.KParameter

inline val KParameter.kClassAny get() = type.classifier as KClass<Any>