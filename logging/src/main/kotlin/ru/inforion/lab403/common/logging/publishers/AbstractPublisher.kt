package ru.inforion.lab403.common.logging.publishers

import ru.inforion.lab403.common.logging.logger.Record

abstract class AbstractPublisher(val name: String) {

    companion object{
        private var mutex: Boolean = false
    }

    open fun publishWrapper(message: String, record: Record){
        if (!mutex) {
            mutex = true
            try {
                publish(message, record)
            } finally {
                mutex = false
            }
        }
    }

    abstract fun publish(message: String, record: Record)

    abstract fun flush()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AbstractPublisher

        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}