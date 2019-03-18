package ru.inforion.lab403.common.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async


/**
 * Created by Alexei Gladkikh on 05/04/17.
 */
fun <T>async(block: suspend CoroutineScope.() -> T): Deferred<T> {
    return GlobalScope.async(block = block)
}
