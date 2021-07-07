package ru.inforion.lab403.common.wsrpc.descs

import java.util.*

internal data class Response(val uuid: UUID, val value: Any?, val exception: String?)
