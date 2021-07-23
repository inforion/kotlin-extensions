@file:Suppress("NOTHING_TO_INLINE")

package unsigned.arrays

import unsigned.types.*

inline fun UIntArray(size: Int, init: (Int) -> UInt) = UIntArray(IntArray(size) { index -> init(index).toInt() })

//inline fun uintArrayOf(vararg elements: UInt): ULongArray = elements

inline fun ULongArray(size: Int, init: (Int) -> ULong) = ULongArray(LongArray(size) { index -> init(index).toLong() })

//inline fun ulongArrayOf(vararg elements: ULong): ULongArray = elements