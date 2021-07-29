package ru.inforion.lab403.common.wsrpc.serde

import com.google.gson.GsonBuilder
import ru.inforion.lab403.common.json.registerTypeAdapter
import java.util.*

fun GsonBuilder.registerBasicClasses(): GsonBuilder {
    registerTypeAdapter(ByteArray::class, ByteArraySerializer)
    registerTypeAdapter(UUID::class, UuidSerializer)
    return this
}