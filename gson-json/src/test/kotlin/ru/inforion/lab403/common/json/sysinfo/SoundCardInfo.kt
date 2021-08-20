package ru.inforion.lab403.common.json.sysinfo

import oshi.hardware.SoundCard

data class SoundCardInfo(
    val driver: String,
    val name: String,
    val codec: String
) {
    constructor(soundCard: SoundCard) : this(
        soundCard.driverVersion,
        soundCard.name,
        soundCard.codec
    )
}