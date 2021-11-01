@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.network

import ru.inforion.lab403.common.extensions.bytes
import java.net.Socket

inline fun Socket.send(data: ByteArray) = outputStream.write(data)

inline fun Socket.send(string: String) = outputStream.write(string.bytes)