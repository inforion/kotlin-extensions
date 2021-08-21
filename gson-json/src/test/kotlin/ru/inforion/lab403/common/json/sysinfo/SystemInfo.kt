package ru.inforion.lab403.common.json.sysinfo

import ru.inforion.lab403.common.extensions.toFile
import ru.inforion.lab403.common.json.sysinfo.FullSystemInfo
import ru.inforion.lab403.common.json.toJson

object SystemInfo {
    @JvmStatic
    fun main(args: Array<String>) {
        val fullSystemInfo = FullSystemInfo()
        fullSystemInfo.toJson(args[1].toFile())
    }
}


