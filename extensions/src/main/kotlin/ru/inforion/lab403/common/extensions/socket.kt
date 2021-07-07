@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions

import java.net.InetSocketAddress

inline operator fun InetSocketAddress.component1(): String = hostName

inline operator fun InetSocketAddress.component2(): Int = port