package ru.inforion.lab403.common.json.sysinfo

import oshi.hardware.HWDiskStore

data class DiskInfo(val model: String, val serial: String) {
    constructor(disk: HWDiskStore) : this(disk.model, disk.serial)
}