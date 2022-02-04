package ru.inforion.lab403.common.extensions

/**
 * Created by Alexei Gladkikh on 02/03/17.
 */

inline fun <reified T : Enum<T>> find(predicate: (item: T) -> Boolean): T? = enumValues<T>().find { predicate(it) }
inline fun <reified T : Enum<T>> first(predicate: (item: T) -> Boolean): T = enumValues<T>().first { predicate(it) }

@Deprecated("Use enum() method")
inline fun <reified T : Enum<T>> convert(ord: Int): T = enumValues<T>()[ord]

inline fun <reified T: Enum<T>> Byte.enum(): T = int_z.enum()
inline fun <reified T: Enum<T>> Short.enum(): T = int_z.enum()
inline fun <reified T: Enum<T>> Int.enum(): T = enumValues<T>()[this]
inline fun <reified T: Enum<T>> Long.enum(): T = int.enum()

inline fun <reified T: Enum<T>> UByte.enum(): T = int_z.enum()
inline fun <reified T: Enum<T>> UShort.enum(): T = int_z.enum()
inline fun <reified T: Enum<T>> UInt.enum(): T = int.enum()
inline fun <reified T: Enum<T>> ULong.enum(): T = int.enum()