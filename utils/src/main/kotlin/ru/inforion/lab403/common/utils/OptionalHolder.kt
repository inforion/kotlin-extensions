package ru.inforion.lab403.common.utils

import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class OptionalHolder<T>(init: T) {

    private var value: T = init

    val orNull: T? get() = value

    val orThrow: T get() = value

    val beforeValueChanged = Signal(this)

    val afterValueChanged = Signal(this)

    private val lock = ReentrantReadWriteLock()

    fun set(newValue: T) = lock.write {
        beforeValueChanged.executeAll()
        value = newValue
        afterValueChanged.executeAll()
    }

    fun locked(action: (T) -> Unit) = lock.read { action(value) }
}