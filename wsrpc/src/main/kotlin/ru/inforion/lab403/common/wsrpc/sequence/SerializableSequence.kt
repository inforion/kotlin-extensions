@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.wsrpc.sequence

@JvmInline
value class SerializableSequence<T>(private val sequence: Sequence<T>) : Sequence<T> {
    companion object {
        inline fun <T> Sequence<T>.asSerializableSequence() = SerializableSequence(this)

        inline fun <T> Iterable<T>.asSerializableSequence() = SerializableSequence(asSequence())
    }

    override fun iterator() = sequence.iterator()
}