package ru.inforion.lab403.common.extensions.buffers

import java.util.concurrent.TimeUnit

interface BlockingBytesIO : BytesIO {
    // non-blocking methods
    fun offer(bytes: ByteArray, offset: Int = 0, count: Int = bytes.size): Int

    fun poll(count: Int): ByteArray

    // non-blocking timeout methods
    fun offer(bytes: ByteArray, offset: Int = 0, count: Int = bytes.size, timeout: Long, unit: TimeUnit = TimeUnit.MILLISECONDS): Int

    fun poll(count: Int, timeout: Long, unit: TimeUnit = TimeUnit.MILLISECONDS): ByteArray

    // blocking methods
    fun put(bytes: ByteArray, offset: Int = 0, count: Int = bytes.size)

    fun take(count: Int): ByteArray
}