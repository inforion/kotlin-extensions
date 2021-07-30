@file:Suppress("NOTHING_TO_INLINE", "UNCHECKED_CAST", "unused")

package ru.inforion.lab403.common.reflection

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.full.withNullability
import kotlin.reflect.jvm.isAccessible
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


inline fun <R, T, D> KProperty1<R, T>.withAccess(block: () -> D): D {
    isAccessible = true
    return block()
}

inline fun <R, T: Any> Collection<KProperty1<out T, *>>.filterWithTypes(cls: Class<out R>) =
    filter { it.type == cls } as Collection<KProperty1<T, R>>

inline val <R, T> KProperty1<R, T>.type: Class<out T> get() = (returnType.classifier as KClass<*>).java as Class<out T>

inline fun <R, T> KProperty1<R, T>.getWithAccess(receiver: R): T = withAccess { get(receiver) }

inline fun <R, T> KProperty1<R, T>.setWithAccess(receiver: R, value: T) = withAccess {
    (this as KMutableProperty1<R, T>).set(receiver, value)
}

inline fun <R, T> KProperty1<*, T>.getDelegateWithAccess(receiver: R) = (this as KProperty1<R, T>).withAccess {
    getDelegate(receiver)
}

inline val <R, T>KProperty1<R, T>.isVariable: Boolean get() = this is KMutableProperty<*>

inline fun <R, T> KProperty1<R, T>.isSubtypeOf(kc: KClass<*>) =
    returnType.withNullability(false).isSubtypeOf(kc.starProjectedType)