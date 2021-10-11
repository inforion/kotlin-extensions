package ru.inforion.lab403.common.utils

import java.net.URL

typealias SignalAction<T> = (T) -> Unit

typealias OnUrlLoadCallback = (URL) -> Unit
