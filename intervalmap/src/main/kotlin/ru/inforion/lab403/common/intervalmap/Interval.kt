package ru.inforion.lab403.common.intervalmap

import ru.inforion.lab403.common.extensions.char
import ru.inforion.lab403.common.extensions.hex

data class Interval(val id: ID, val first: Mark, val last: Mark) {
    companion object {
        var idFormatter = { id: ID -> id.char }
        var valueFormatter = { mark: Mark -> "0x${mark.hex}" }
    }

    constructor(id: Char, first: Mark, last: Mark) : this(id.id, first, last)

    override fun toString() = "${idFormatter(id)}[${valueFormatter(first)}..${valueFormatter(last)}]"
}