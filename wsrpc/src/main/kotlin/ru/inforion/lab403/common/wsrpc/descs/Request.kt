package ru.inforion.lab403.common.wsrpc.descs

import java.util.*

internal data class Request(
    val uuid: UUID,
    val endpoint: UUID,
    val method: String,
    val values: Parameters
)
