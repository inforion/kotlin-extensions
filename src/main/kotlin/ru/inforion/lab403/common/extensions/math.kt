@file:Suppress("unused")

package ru.inforion.lab403.common.extensions

import java.util.*

object math {
    data class Bin<out T>(val value: T, val count: Int)

    fun histogram(data: ByteArray): List<Bin<Byte>> {
        val values = data.toSet()
        val result = ArrayList<Bin<Byte>>(values.size)
        values.forEach { byte -> result.add(Bin(byte, data.count { it == byte })) }
        return result
    }

    fun histogram(data: ByteArray, mask: BooleanArray): List<Bin<Byte>> {
        val values = data.toSet()
        val result = ArrayList<Bin<Byte>>(values.size)
        val filtered = data.filterIndexed { i, _ -> mask[i] }
        values.forEach { byte ->
            val bin = Bin(byte, filtered.count { it == byte })
            result.add(bin)
        }
        return result
    }

    fun histogram(values: Set<Byte>, data: ByteArray, mask: BooleanArray): IntArray {
        val result = IntArray(256)
        val filtered = data.filterIndexed { i, _ -> mask[i] }
        values.forEach { byte -> result[byte.int_z] = filtered.count { it == byte } }
        return result
    }

    fun logRandomBase(index: Double, base: Double): Double =  Math.log(index) / Math.log(base)

    fun loge(index: Double): Double = Math.log(index)
    fun log2(index: Double): Double = logRandomBase(index, 2.0)
    fun log10(index: Double): Double = Math.log10(index)
}

fun Double.round(precision: Double = 1.0): Double = Math.round(this / precision) * precision
fun Float.round(precision: Float = 1.0f): Float = Math.round(this / precision) * precision