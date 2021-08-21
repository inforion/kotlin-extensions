package ru.inforion.lab403.common.json.sysinfo

import oshi.hardware.Baseboard

data class MotherboardInfo(
    val manufacturer: String,
    val model: String,
    val version: String,
    val serial: String
) {
    constructor(baseboard: Baseboard) : this(
        baseboard.manufacturer,
        baseboard.model,
        baseboard.version,
        baseboard.serialNumber
    )
}