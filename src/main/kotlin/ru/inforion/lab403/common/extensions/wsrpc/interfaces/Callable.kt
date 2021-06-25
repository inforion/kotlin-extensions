package ru.inforion.lab403.common.extensions.wsrpc.interfaces

fun interface Callable<T> {
    fun call(vararg arg: Any?): T
}