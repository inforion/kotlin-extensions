@file:Suppress("unused")

package ru.inforion.lab403.common.scanner

import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import ru.inforion.lab403.common.extensions.packageName
import kotlin.reflect.KClass

inline fun <reified T : Any> String.scanSubtypesOf(cl: ClassLoader? = null): Set<Class<out T>> {
    val url = ClasspathHelper.forPackage(this)
    val builder = ConfigurationBuilder().apply {
        addUrls(url)
        addScanners(Scanners.SubTypes)
        if (cl != null) addClassLoaders(cl)
    }
    return Reflections(builder).getSubTypesOf(T::class.java)
}

inline fun <reified T: Any> Class<T>.scanSubtypesOf(cl: ClassLoader? = null) = name.packageName().scanSubtypesOf<T>(cl)

inline fun <reified T: Any> KClass<T>.scanSubtypesOf(cl: ClassLoader? = null) = java.scanSubtypesOf(cl)
