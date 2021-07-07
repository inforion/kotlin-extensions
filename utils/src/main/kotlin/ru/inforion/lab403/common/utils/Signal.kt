package ru.inforion.lab403.common.utils

typealias SignalAction<T> = (T) -> Unit

class Signal<T>(private val parent: T) {
    private val actions = mutableListOf<SignalAction<T>>()

    fun executeAll() = actions.forEach { it.invoke(parent) }

    operator fun invoke(callback: (T) -> Unit): T {
        actions.add(callback)
        return parent
    }
}