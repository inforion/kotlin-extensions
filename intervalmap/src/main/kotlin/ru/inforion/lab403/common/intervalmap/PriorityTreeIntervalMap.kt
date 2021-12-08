@file:Suppress("NOTHING_TO_INLINE", "MemberVisibilityCanBePrivate", "unused")

package ru.inforion.lab403.common.intervalmap

import ru.inforion.lab403.common.extensions.hex
import ru.inforion.lab403.common.extensions.str
import ru.inforion.lab403.common.logging.logger
import java.util.*


class PriorityTreeIntervalMap(val name: String = "Map-${idGenerator++}") : Iterable<Entry> {
    companion object {
        private var idGenerator = 0

        val log = logger()
    }

    private val map = TreeMap<Mark, MutableList<Interval>>()
    private val intervals = mutableMapOf<ID, Interval>()

    private var baseOrNull: Interval? = null

    val isInitialized get() = baseOrNull != null

    val base get() = requireNotNull(baseOrNull) { "Map was not initialized" }

    constructor(id: ID, first: Mark, last: Mark) : this() {
        init(id, first, last)
    }

    constructor(id: Char, first: Mark, last: Mark) : this() {
        init(id.id, first, last)
    }

    fun init(id: ID, first: Mark, last: Mark): Interval {
        check(intervals.isEmpty()) { "$name: can't init already initialized Map" }

        baseOrNull = Interval(id, first, last)
        intervals[id] = base

        map[first] = mutableListOf(base)

        if (last != MARK_MAX_VALUE) {
            map[last + 1u] = mutableListOf()
        }

        log.fine { "$name: initialized with $base" }

        return base
    }

    fun clear() {
        map.clear()
        intervals.clear()
        baseOrNull = null
    }

    fun add(id: ID, first: Mark, last: Mark): Interval {
        require(id != base.id) {
            "Can't map region with init region id = $base"
        }

        val interval = intervals[id]

        if (interval != null) {
            log.fine { "$name: $interval will be remapped" }
            doUnmap(interval)
        }

        return Interval(id, first, last).also { doMap(it) }
    }

    fun add(id: Char, first: Mark, last: Mark): Interval = add(id.id, first, last)

    fun remove(id: ID): Interval? {
        require(id != base.id) {
            "Can't unmap region with init region id = $base, clear all map for it"
        }

        val interval = intervals[id]

        if (interval == null) {
            log.warning { "$name: can't unmap region with id=$id because not mapped" }
            return null
        }

        return interval.also { doUnmap(it) }
    }

    fun remove(id: Char) = remove(id.id)

    operator fun get(key: Mark): Interval {
        val intervals = map.left(key)
        check(intervals.isNotEmpty()) { "$name: no intervals mapped at 0x${key.hex}" }
        return intervals.last()
    }

    private fun List<Mark>.mapIfRequired(interval: Interval) = asSequence()
        .mapNotNull { mark -> map.at(mark).takeIf { it.isNotEmpty() } }
        .filter { it.last() != interval }
        .forEach { it.add(interval) }

    private fun doMap(interval: Interval) {
        intervals[interval.id] = interval

        if (interval.last == MARK_MAX_VALUE) {
            val marks = map.from(interval.first)
            val s2l = map.left(interval.first)

            val s2lCopy = s2l.copy()

            map[interval.first] = s2lCopy
            s2lCopy.add(interval)

            marks.mapIfRequired(interval)
        } else {
            val marks = map.between(interval.first, interval.last + 1u)

            val s2l = map.left(interval.first)
            val e2l = map.left(interval.last + 1u)

            val s2lCopy = s2l.copy()
            val e2lCopy = e2l.copy()

            map[interval.first] = s2lCopy
            map[interval.last + 1u] = e2lCopy

            s2lCopy.add(interval)

            marks.mapIfRequired(interval)
        }

        log.fine { "$name: add mapping $interval" }
    }

    private fun doUnmap(interval: Interval) {
        intervals.remove(interval.id)

        // remove the interval from the mapping
        map.values.forEach {
            val index = it.indexOf(interval)
            if (index != -1) {
                require(index == it.lastIndex) {
                    "Unmapping of interval $interval that isn't on top of current mapping not supported"
                }
                it.removeAt(index)
            }
        }

        // cleanup the map
        val end = base.last + 1u
        map.entries.removeIf { (mark, intervals) ->
            mark != end && intervals.isEmpty()
        }

        log.fine { "$name: remove mapping $interval" }
    }

    override fun iterator() = map
        .asSequence()
        .associate { it.key to it.value }
        .iterator()

    override fun toString() = joinToString("\n") {
        "${it.key}=[${it.value.joinToString("") { i -> i.id.str }}]"
    }
}