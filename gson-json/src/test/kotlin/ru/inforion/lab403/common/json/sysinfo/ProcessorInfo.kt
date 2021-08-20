package ru.inforion.lab403.common.json.sysinfo

import oshi.hardware.CentralProcessor

data class ProcessorInfo(
    val vendor: String,
    val name: String,
    val id: String,
    val model: String,
    val family: String
) {
    constructor(cpu: CentralProcessor) : this(
        cpu.vendor,
        cpu.name,
        cpu.processorID,
        cpu.model,
        cpu.family
    )
}