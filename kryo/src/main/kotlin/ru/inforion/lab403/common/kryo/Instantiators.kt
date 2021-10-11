package ru.inforion.lab403.common.kryo

import com.esotericsoftware.kryo.KryoException
import com.esotericsoftware.reflectasm.ConstructorAccess
import org.objenesis.instantiator.ObjectInstantiator
import ru.inforion.lab403.common.logging.logger

internal object Instantiators {

    val log = logger()

    fun <T> newOrElse(
        cls: Class<T>,
        it: Iterable<(Class<T>) -> ObjectInstantiator<T>?>,
        elsefn: ObjectInstantiator<T>
    ) = it.firstNotNullOfOrNull { it(cls) } ?: elsefn

    private fun <T> forClass(t: Class<T>, fn: () -> T) = ObjectInstantiator {
        runCatching { fn() }.getOrElse {
            throw KryoException("Error constructing instance of class: ${t.name}", it)
        }
    }

    private fun <T> getConstructor(c: Class<T>) = runCatching {
        c.getConstructor()
    }.getOrElse {
        c.getDeclaredConstructor().apply { isAccessible = true }
    }

    fun <T> reflectAsm(t: Class<T>): ObjectInstantiator<T>? = runCatching {
        val access = ConstructorAccess.get(t)
        // Try it once, because this isn't always successful:
        access.newInstance()
        // Okay, looks good:
        forClass(t) { access.newInstance() }
    }.onFailure {
        log.finer { "$it" }
    }.getOrNull()

    fun <T> normalJava(t: Class<T>): ObjectInstantiator<T>? = runCatching {
        val cons = getConstructor(t)
        forClass(t) { cons.newInstance() }
    }.onFailure {
        log.finer { "$it" }
    }.getOrNull()
}