package unsigned.internal

import unsigned.types.*

/** An iterator over a sequence of values of type `UByte`. */
abstract class UByteIterator : Iterator<UByte> {
    final override fun next() = nextUByte()

    /** Returns the next value in the sequence without boxing. */
    abstract fun nextUByte(): UByte
}

/** An iterator over a sequence of values of type `UShort`. */
abstract class UShortIterator : Iterator<UShort> {
    final override fun next() = nextUShort()

    /** Returns the next value in the sequence without boxing. */
    abstract fun nextUShort(): UShort
}

/** An iterator over a sequence of values of type `UInt`. */
abstract class UIntIterator : Iterator<UInt> {
    final override fun next() = nextUInt()

    /** Returns the next value in the sequence without boxing. */
    abstract fun nextUInt(): UInt
}

/** An iterator over a sequence of values of type `ULong`. */
abstract class ULongIterator : Iterator<ULong> {
    final override fun next() = nextULong()

    /** Returns the next value in the sequence without boxing. */
    abstract fun nextULong(): ULong
}
