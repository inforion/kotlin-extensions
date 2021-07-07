package ru.inforion.lab403.common.wsrpc.interfaces

fun interface Callable<T> {
    fun call(vararg arg: Any?): T
}