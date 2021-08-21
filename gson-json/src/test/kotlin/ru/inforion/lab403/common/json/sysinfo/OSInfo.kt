package ru.inforion.lab403.common.json.sysinfo

import oshi.software.os.OperatingSystem

data class OSInfo(
    val manufacturer: String,
    val family: String,
    val version: String,
    val codeName: String,
    val buildNumber: String
) {
    constructor(os: OperatingSystem) : this(
        os.manufacturer,
        os.family,
        os.version.version,
        os.version.codeName,
        os.version.buildNumber
    )
}