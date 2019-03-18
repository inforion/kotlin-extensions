@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions

/**
 * Created by Alexei Gladkikh on 25/06/16.
 */

enum class Frequency(val multiplier: Long) {
    Hz(1),
    kHz(1_000),
    MHz(1_000_000),
    GHz(1_000_000_000);
}

/**
 * Multipliers in readable form for different frequency dimensions
 */
inline val Number.Hz get() = this.toLong()
inline val Number.kHz get() = (toDouble() * Frequency.kHz.multiplier).toLong()
inline val Number.MHz get() = (toDouble() * Frequency.MHz.multiplier).toLong()
inline val Number.GHz get() = (toDouble() * Frequency.GHz.multiplier).toLong()

/**
 * Extension functions for conversion between frequencies dimensions
 */
inline fun Number.to_Hz(from: Frequency = Frequency.Hz): Long = (toDouble() * from.multiplier / Frequency.Hz.multiplier).toLong()
inline fun Number.to_kHz(from: Frequency = Frequency.Hz): Long = (toDouble() * from.multiplier / Frequency.kHz.multiplier).toLong()
inline fun Number.to_MHz(from: Frequency = Frequency.Hz): Long = (toDouble() * from.multiplier / Frequency.MHz.multiplier).toLong()
inline fun Number.to_GHz(from: Frequency = Frequency.Hz): Long = (toDouble() * from.multiplier / Frequency.GHz.multiplier).toLong()

enum class Time(val divider: Long) {
    s(1),
    ms(1_000),
    us(1_000_000),
    ns(1_000_000_000);
}

fun Number.to_s(from: Time = Time.s): Long = (toDouble() * Time.s.divider / from.divider).toLong()
fun Number.to_ms(from: Time = Time.s): Long = (toDouble() * Time.ms.divider / from.divider).toLong()
fun Number.to_us(from: Time = Time.s): Long = (toDouble() * Time.us.divider / from.divider).toLong()
fun Number.to_ns(from: Time = Time.s): Long = (toDouble() * Time.ns.divider / from.divider).toLong()

inline val Number.s get() = this.toLong()
inline val Number.ms get() = (toDouble() / Time.ms.divider).toLong()
inline val Number.us get() = (toDouble() / Time.us.divider).toLong()
inline val Number.ns get() = (toDouble() / Time.ns.divider).toLong()
