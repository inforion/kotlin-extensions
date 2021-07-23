@file:Suppress("NOTHING_TO_INLINE", "UNCHECKED_CAST")

package ru.inforion.lab403.common.extensions

import java.lang.reflect.Field
import kotlin.reflect.*
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.full.withNullability
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

@Suppress("UNCHECKED_CAST")
inline fun <T: Any>KParameter.kClass(): KClass<T> = type.classifier as KClass<T>

inline val KParameter.kClassAny get() = kClass<Any>()

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


/**
 * {EN}
 * Implements 'reverse' [Class.isAssignableFrom] for type of [KProperty1] field
 *
 * @param cls basic Java class
 *
 *
 * @return true if type of field is subtype of specified class [cls]
 * {EN}
 */

fun KType.stringify() = this.toString().className()

fun <T> KCallable<T>.stringify() = buildString {
    val visibility = visibility
    if (visibility != null) {
        visibility.ordinal
        append(visibility.name.lowercase())
        append(" ")
    }

    if (this@stringify !is KProperty<*>) {
        append("fun ")
        append(name)
        append("(")
        val params = parameters
            .filter { it.name != null }
            .joinToString(", ") { "${it.name}: ${it.type.stringify()}" }
        append(params)
        append(")")
    } else {
        if (isLateinit) append("lateinit ")
        if (isConst) append("val ") else append("var ")
        append(name)
    }
    append(": ")
    append(returnType.stringify())
}

fun <T: Any> KClass<T>.new(vararg args: Any?) = constructors.first().call(args)

fun <T: Any> KClass<T>.new(vararg args: Any?, predicate: (KFunction<T>) -> Boolean) =
    constructors.first(predicate).call(args)

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


inline fun <T: Number, reified R> T.convert(): R = when (R::class) {
    ULong::class -> ulong
    UInt::class -> uint
    UShort::class -> ushort
    UByte::class -> ubyte
    Long::class -> long
    Int::class -> int
    Short::class -> short
    Byte::class -> byte
    Double::class -> double
    Float::class -> float
    Char::class -> char
    else -> error("")
} as R

inline fun Class<*>.isKotlinClass() = declaredAnnotations.any { it.annotationClass.java.name == "kotlin.Metadata" }