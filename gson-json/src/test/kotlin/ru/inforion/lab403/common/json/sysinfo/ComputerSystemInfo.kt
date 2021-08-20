package ru.inforion.lab403.common.json.sysinfo

import oshi.hardware.ComputerSystem

data class ComputerSystemInfo(
    val manufacturer: String,
    val model: String,
    val serial: String
) {
    constructor(computer: ComputerSystem) : this(
        computer.manufacturer,
        computer.model,
        computer.serialNumber
    )
}