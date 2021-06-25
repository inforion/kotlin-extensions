package ru.inforion.lab403.common.extensions.concurrent.locks

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock

object PhonyLock : Lock {
    override fun lock() = Unit

    override fun unlock() = Unit

    override fun lockInterruptibly() = Unit

    override fun tryLock() = true

    override fun tryLock(time: Long, unit: TimeUnit) = true

    override fun newCondition() = throw NotImplementedError("PhonyLock may not have conditions")
}