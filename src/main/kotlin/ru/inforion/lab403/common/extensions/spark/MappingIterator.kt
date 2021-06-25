package ru.inforion.lab403.common.extensions.spark

class MappingIterator<T, R>(
        private val self: Iterator<T>,
        private val func: (Int, T) -> R,
        private var index: Int = 0
) : AbstractIterator<R>() {
    override fun computeNext() = if (self.hasNext()) setNext(func(index++, self.next())) else done()
}