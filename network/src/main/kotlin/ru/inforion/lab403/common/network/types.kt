package ru.inforion.lab403.common.network

import java.net.InetAddress
import java.net.Socket

typealias OnStartCallback = (address: InetAddress) -> Unit
typealias OnConnectCallback = Socket.() -> Boolean
typealias OnDisconnectCallback = Socket.() -> Unit
typealias OnReceiveCallback = Socket.(buffer: ByteArray) -> Boolean