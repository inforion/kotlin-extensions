@file:Suppress("NOTHING_TO_INLINE", "UNCHECKED_CAST", "unused")

package ru.inforion.lab403.common.reflection

import ru.inforion.lab403.common.extensions.className
import kotlin.reflect.*

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