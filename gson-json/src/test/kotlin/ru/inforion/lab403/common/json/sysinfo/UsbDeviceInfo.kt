package ru.inforion.lab403.common.json.sysinfo

import oshi.hardware.UsbDevice

data class UsbDeviceInfo(
    val name: String,
    val vendor: String,
    val vendorId: String,
    val productId: String,
    val serial: String
) {
    constructor(usb: UsbDevice) : this(
        usb.name,
        usb.vendor,
        usb.vendorId,
        usb.productId,
        usb.serialNumber
    )
}