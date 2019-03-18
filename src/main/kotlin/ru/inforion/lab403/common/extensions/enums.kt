package ru.inforion.lab403.common.extensions

/**
 * Created by Alexei Gladkikh on 02/03/17.
 */

inline fun <reified T : Enum<T>> find(predicate: (item: T) -> Boolean): T? = enumValues<T>().find { predicate(it) }
inline fun <reified T : Enum<T>> first(predicate: (item: T) -> Boolean): T = enumValues<T>().first { predicate(it) }

inline fun <reified T : Enum<T>> convert(ord: Int): T = enumValues<T>()[ord]