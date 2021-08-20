package ru.inforion.lab403.common.json.sysinfo

import oshi.hardware.Firmware

data class BiosInfo(
    val manufacturer: String,
    val name: String,
    val description: String,
    val version: String,
    val releaseDate: String
) {
    constructor(fw: Firmware) : this(
        fw.manufacturer,
        fw.name,
        fw.description,
        fw.version,
        fw.releaseDate
    )
}