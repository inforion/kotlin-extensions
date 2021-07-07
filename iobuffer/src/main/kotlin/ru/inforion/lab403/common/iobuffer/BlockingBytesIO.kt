package ru.inforion.lab403.common.iobuffer

import java.util.concurrent.TimeUnit

/**
 * Blocking bytes buffer input-output interface
 *
 * @since 0.3.4
 */
interface BlockingBytesIO : BytesIO {

    // non-blocking methods

    /**
     * Writes **non-blocking** [count] specified [bytes] into buffer and returns number of written bytes
     */
    fun offer(bytes: ByteArray, offset: Int = 0, count: Int = bytes.size): Int

    /**
     * Read **non-blocking** [count] bytes from buffer and returns it
     */
    fun poll(count: Int): ByteArray

    // non-blocking timeout methods

    /**
     * Writes **non-blocking with timeout** [count] specified [bytes] into buffer and returns number of written bytes
     *
     * NOTE: this method works like socket, i.e. it writes as soon as any space in buffer becomes available
     */
    fun offer(
        bytes: ByteArray,
        offset: Int = 0,
        count: Int = bytes.size,
        timeout: Long,
        unit: TimeUnit = TimeUnit.MILLISECONDS
    ): Int

    /**
     * Read **non-blocking with timeout** [count] bytes from buffer and returns it
     *
     * NOTE: this method works like socket, i.e. it read as soon as any bytes in buffer becomes available
     */
    fun poll(count: Int, timeout: Long, unit: TimeUnit = TimeUnit.MILLISECONDS): ByteArray

    // blocking methods

    /**
     * Writes **blocking** [count] specified [bytes] into buffer and returns number of written bytes
     *
     * NOTE: this method blocks forever until all required space not available
     */
    fun put(bytes: ByteArray, offset: Int = 0, count: Int = bytes.size)

    /**
     * Read **blocking** [count] bytes from buffer and returns it
     *
     * NOTE: this method blocks forever until all required bytes not available
     */
    fun take(count: Int): ByteArray
}