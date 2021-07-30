@file:Suppress("unused")

package ru.inforion.lab403.common.scanner

import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.util.ClasspathHelper
import ru.inforion.lab403.common.extensions.packageName
import kotlin.reflect.KClass

inline fun <reified T: Any> String.scanSubtypesOf(): Set<Class<out T>> {
    val urls = ClasspathHelper.forPackage(this)
    val reflections = Reflections(urls, SubTypesScanner())
    return reflections.getSubTypesOf(T::class.java)
}

inline fun <reified T: Any> Class<T>.scanSubtypesOf() = name.packageName().scanSubtypesOf<T>()

inline fun <reified T: Any> KClass<T>.scanSubtypesOf() = java.scanSubtypesOf()
