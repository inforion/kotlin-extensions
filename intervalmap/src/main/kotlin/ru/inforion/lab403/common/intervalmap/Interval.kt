package ru.inforion.lab403.common.intervalmap

import ru.inforion.lab403.common.extensions.hex
import ru.inforion.lab403.common.extensions.hex8

data class Interval(val id: ID, val first: Mark, val last: Mark) {
    companion object {
        var idFormatter = { id: ID -> id.hex }
        var markFormatter = { mark: Mark -> "0x${mark.hex8}" }
    }

    constructor(id: Char, first: Mark, last: Mark) : this(id.id, first, last)

    override fun toString() = "${idFormatter(id)}[${markFormatter(first)}..${markFormatter(last)}]"
}