package ru.inforion.lab403.common.extensions

import java.io.Serializable

fun interface SerializableComparator<T> : Comparator<T>, Serializable