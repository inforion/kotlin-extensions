package ru.inforion.lab403.common.intervalmap

import ru.inforion.lab403.common.extensions.int_s

typealias ID = Int
typealias Mark = ULong
typealias Ranges = MutableList<Interval>
typealias Entry = Map.Entry<ULong, List<Interval>>

inline val Char.id get() = int_s