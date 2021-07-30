@file:Suppress("unused", "NOTHING_TO_INLINE")

package ru.inforion.lab403.common.reflection

import java.lang.reflect.Field

fun Class<*>.findFieldRecursive(name: String): Field? {
    if (this == Any::class.java)
        return null

    try {
        return getDeclaredField(name)
    } catch (ex: NoSuchFieldException) {
        check(superclass != null) { "Superclass must not be null" }

        val scResult = superclass.findFieldRecursive(name)
        if (scResult != null)
            return scResult

        interfaces.forEach {
            val iResult = it.findFieldRecursive(name)
            if (iResult != null)
                return iResult
        }

        return null
    }
}

inline fun Class<*>.isKotlinClass() = declaredAnnotations.any { it.annotationClass.java.name == "kotlin.Metadata" }