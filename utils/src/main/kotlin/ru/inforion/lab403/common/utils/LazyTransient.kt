package ru.inforion.lab403.common.utils

import java.io.Serializable
import kotlin.reflect.KProperty

class LazyTransient<R, T>(private val initializer: () -> T): Serializable {
    @Transient private var value: T? = null
    @Transient private var initialized: Boolean? = false

    operator fun getValue(thisRef: R, property: KProperty<*>): T {
        val initializedOrNotNull = initialized ?: false
        if (!initializedOrNotNull) {
            initialized = true
            value = initializer()
        }
        return value!!
    }
}