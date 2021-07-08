package ru.inforion.lab403.common.swarm.tasks

import ru.inforion.lab403.common.swarm.common.Slave
import ru.inforion.lab403.common.swarm.interfaces.ITask

internal class IndexedCommonTask<T, R>(val index: Int, val value: T, val transform: (T) -> R) : ITask {
    override fun execute(slave: Slave) {
        val result = transform(value)
        slave.response(result, index)
    }
}