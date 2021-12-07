@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.iobuffer

inline fun BytesIO.isEmpty() = readAvailable == 0

inline fun BytesIO.isNotEmpty() = !isEmpty()