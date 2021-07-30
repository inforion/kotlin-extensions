package ru.inforion.lab403.common.wsrpc.serde

import com.google.gson.GsonBuilder
import ru.inforion.lab403.common.json.registerTypeAdapter

fun GsonBuilder.registerBasicClasses() = apply {
    registerTypeAdapter(ByteArray::class, ByteArraySerializer)
}