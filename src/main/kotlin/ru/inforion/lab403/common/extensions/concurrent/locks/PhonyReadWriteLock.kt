@file:Suppress("unused")

package ru.inforion.lab403.common.extensions.concurrent.locks

import java.util.concurrent.locks.ReadWriteLock

object PhonyReadWriteLock : ReadWriteLock {
    private val readLock = PhonyLock
    private val writeLock = PhonyLock

    override fun readLock() = readLock
    override fun writeLock() = writeLock
}
