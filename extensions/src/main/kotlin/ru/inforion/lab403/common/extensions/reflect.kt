package ru.inforion.lab403.common.extensions

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.javaType

@OptIn(ExperimentalStdlibApi::class)
inline val KType.java get() =
/**
 * Works as kotlin.reflect.javaType
 * Workflow for unsigned types (ULong.javaType can return long)
 */
    when (val cls = this.classifier) {
        is KClass<*> -> cls.java
        else -> javaType
    }
