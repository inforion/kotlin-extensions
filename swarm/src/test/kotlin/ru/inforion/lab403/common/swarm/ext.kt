package ru.inforion.lab403.common.swarm

import ru.inforion.lab403.common.extensions.hexlify
import ru.inforion.lab403.common.extensions.sha256

internal fun sha256Test(value: Int, count: Int): String {
    var result = value.toString()
    repeat(count) { result = result.sha256().hexlify() }
    return result
}