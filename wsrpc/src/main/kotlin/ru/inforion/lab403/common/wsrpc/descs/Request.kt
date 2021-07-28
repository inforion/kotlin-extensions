package ru.inforion.lab403.common.wsrpc.descs

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import java.util.*

@Serializable
internal data class Request(
    @Contextual val uuid: UUID,
    @Contextual val endpoint: UUID,
    val method: String,
    val values: Map<String, JsonElement>
)
