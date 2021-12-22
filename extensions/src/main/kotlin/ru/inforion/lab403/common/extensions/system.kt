@file:Suppress("unused")

package ru.inforion.lab403.common.extensions

import java.io.Console
import java.net.InetAddress
import java.util.*

val Any.identity get(): Int = System.identityHashCode(this)

inline val availableProcessors get() = Runtime.getRuntime().availableProcessors()

inline val machineHostName: String get() = InetAddress.getLocalHost().hostName

inline val machineHostAddress: String get() = InetAddress.getLocalHost().hostAddress

inline val operatingSystemName: String get() = System.getProperty("os.name")

inline val operatingSystemTag: String get() = operatingSystemName.lowercase()

inline val isWindowsOperatingSystem get() = "win" in operatingSystemTag

inline val isLinuxOperatingSystem get() =
    "nix" in operatingSystemTag || "nux" in operatingSystemTag || "aix" in operatingSystemTag

inline val isMacOperatingSystem get() = "mac" in operatingSystemTag

inline val isSolarisOperatingSystem get() = "sunos" in operatingSystemTag

inline val javaVersion: String get() = System.getProperty("java.version")

inline val javaClassPath: String get() = System.getProperty("java.class.path")

inline val userHomeDir: String get() = System.getProperty("user.home")

inline val javaWorkingDirectory: String get() = System.getProperty("user.dir")

inline val javaHome: String get() = System.getProperty("java.home")

inline val environment: Map<String, String> get() = System.getenv()

inline val properties: Properties get() = System.getProperties()

inline val runtime: Runtime get() = Runtime.getRuntime()

inline val currentTimeMillis: Long get() = System.currentTimeMillis()

inline val currentThread: Thread get() = Thread.currentThread()

inline val systemConsole: Console get() = System.console()