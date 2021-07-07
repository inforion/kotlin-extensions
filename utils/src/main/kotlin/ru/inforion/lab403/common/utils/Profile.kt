@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.utils

/**
 * Simple wrapper for code profiling purpose
 *
 * @since 0.3.4
 */
class Profile(val name: String) {
    private inline fun time() = System.nanoTime()

    private inline fun accumulate(previous: Double, sample: Long, count: Long) =
        ((count - 1) * previous + sample) / count

    var begin: Long = 0
        private set

    var end: Long = 0
        private set

    var instant: Long = 0
        private set

    var total: Long = 0
        private set

    var max: Long = 0
        private set

    var average: Double = 0.0
        private set

    var count: Long = 0
        private set

    var tag: Int = 0
        private set

    fun start() {
        begin = time()
    }

    fun update(value: Int = 0) {
        if (begin == 0L) return
        count += 1
        end = time()

        tag = value

        instant = end - begin
        average = accumulate(average, instant, count)
        max = maxOf(max, instant)
        total += instant
    }

    inline fun <T> measure(condition: Boolean = true, value: Int = 0, action: () -> T): T {
        if (!condition)
            return action()

        start()
        val result = action()
        update(value)
        return result
    }

    override fun toString() = "%s[avg=%d last=%d max=%d count=%d]"
        .format(name, average.toLong(), instant, max, count)
}
