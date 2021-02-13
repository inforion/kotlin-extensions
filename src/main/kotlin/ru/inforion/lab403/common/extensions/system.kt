package ru.inforion.lab403.common.extensions

val Any.identity get(): Int = System.identityHashCode(this)