package ru.inforion.lab403.common.json.sysinfo

import oshi.hardware.HardwareAbstractionLayer

data class HardwareInfo(
    val cpu: ProcessorInfo,
    val bios: BiosInfo,
    val motherboard: MotherboardInfo,
    val computerSystem: ComputerSystemInfo,
    val disks: List<DiskInfo>,
    val soundCards: List<SoundCardInfo>,
    val networkInterfaces: List<NetworkInterfaceInfo>,
    val usbDevices: List<UsbDeviceInfo>
) {
    constructor(hw: HardwareAbstractionLayer) : this(
        ProcessorInfo(hw.processor),
        BiosInfo(hw.computerSystem.firmware),
        MotherboardInfo(hw.computerSystem.baseboard),
        ComputerSystemInfo(hw.computerSystem),
        hw.diskStores.map { DiskInfo(it) }.distinct(),
        hw.soundCards.map { SoundCardInfo(it) }.distinct(),
        hw.networkIFs.map { NetworkInterfaceInfo(it) }.distinct(),
        hw.getUsbDevices(true).map { UsbDeviceInfo(it) }
    )
}