@file:Suppress("UNCHECKED_CAST")

package ru.inforion.lab403.common.extensions

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField

data class DelegatedProperty<T: Any, D: Any>(val property: KProperty1<T, *>, val delegatingToInstance: D)

/**
 * Warning: Don't use this function with non-final instances!
 */
inline fun <reified T: Any, D: Any>findDelegatingPropertyInstances(
        instance: T,
        delegatingTo: KClass<D>
): List<DelegatedProperty<T, D>> {
    val klass = T::class
    val properties = klass.declaredMemberProperties
    return properties.filter {
        val cls = delegatingTo.java
        val field = it.javaField ?: return@filter false
        val type = field.type
        val isAssignableFrom = type.isAssignableFrom(cls)
        isAssignableFrom
    }.map { DelegatedProperty(it, it.get(instance) as D) }
}