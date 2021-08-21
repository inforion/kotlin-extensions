package ru.inforion.lab403.common.json.sysinfo

import oshi.hardware.NetworkIF

data class NetworkInterfaceInfo(
    val name: String,
    val displayName: String,
    val mac: String,
    val index: Int
) {
    constructor(network: NetworkIF) : this(
        network.name,
        network.displayName,
        network.macaddr,
        network.networkInterface.index
    )
}