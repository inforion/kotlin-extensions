package ru.inforion.lab403.common.json.sysinfo

import oshi.SystemInfo
import ru.inforion.lab403.common.extensions.bytes
import ru.inforion.lab403.common.extensions.hexlify
import java.security.MessageDigest

data class FullSystemInfo(
    val operationSystem: OSInfo,
    val hardware: HardwareInfo
) {
    constructor(systemInfo: SystemInfo = SystemInfo()) : this(
        OSInfo(systemInfo.operatingSystem),
        HardwareInfo(systemInfo.hardware)
    )

    override fun toString() = "${operationSystem.manufacturer}-${operationSystem.family};" +
            "${hardware.cpu.name}-${hardware.cpu.id};" +
            "${hardware.motherboard.manufacturer}-${hardware.motherboard.model}-" +
            "${hardware.motherboard.version}-${hardware.motherboard.serial};" +
            "${hardware.computerSystem.model}-${hardware.computerSystem.serial}"

    fun token(): String {
        val sha1 = MessageDigest.getInstance("SHA1")
        return sha1.digest(toString().bytes).hexlify()
    }
}

