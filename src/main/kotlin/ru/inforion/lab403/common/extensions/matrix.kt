@file:Suppress("unused", "NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions

import org.jblas.ComplexDouble
import org.jblas.ComplexDoubleMatrix
import org.jblas.DoubleMatrix
import java.util.*

/**
 * Created by Alexei Gladkikh on 11/02/17.
 */

data class Format(
        var formatter: String = "%3.1f",
        var delimiter: Char = ' ',
        var line: Char = '\n',
        var mult: Double = 1.0,
        var skipZeros: Boolean = false,
        var outRowNumber: Boolean = false,
        var outColNumber: Boolean = false,
        var colorize: Boolean = false,
        var outEdges: Boolean = false)

val format = Format()

fun DoubleMatrix.stringify(
    formatter: String = format.formatter,
    delimiter: Char = format.delimiter,
    line: Char = format.line,
    mult: Double = format.mult,
    outRowNumber: Boolean = format.outRowNumber,
    outColNumber: Boolean = format.outColNumber,
    outEdges: Boolean = format.outEdges,
    colorize: Boolean = format.colorize,
    skipZeros: Boolean = format.skipZeros): String {

    val ANSI_RED = "\u001B[31m"
//    val ANSI_YELLOW = "\u001B[33m"
//    val ANSI_GREEN = "\u001B[32m"
    val ANSI_BLUE = "\u001B[34m"
    val ANSI_RESET = "\u001B[0m"

    val precision = if ('.' !in formatter) 0.01
        else Math.pow(10.0, -formatter.substringBeforeLast('f').split('.')[1].toDouble())

    val blank = { s: String -> " ".repeat(s.length) }

    return buildString {

        var max = Double.MAX_VALUE
        var min = Double.MIN_VALUE
        if (colorize) {
            max = this@stringify.max()
            min = this@stringify.min()
        }

        var foundCrosses = false
        val crossPositions = HashSet<Int>()

        val makeBorder = { n: Int ->
            if (outEdges) {
                this.insert(0, line)
                repeat(n) { this.insert(it, if (it in crossPositions) '+' else '-') }
            }
        }

        val makeLine = { n: Int ->
            if (outEdges) {
                repeat(n) { this.append(if (it in crossPositions) '+' else '-') }
                this.append(line)
            }
        }

        val makeColumn = { if (outEdges) {
            if (!foundCrosses)
                crossPositions.add(this.length)
            this.append('|')
            this.append(delimiter)
        }}

        var lineSize = -1

        val rlen = "$rows".length
        val rfrmt = "%${rlen}s"
        val clen = Math.max(formatter.format(columns.toFloat()).length, "$columns".length)
        val cfrmt = "%${clen}s"
        if (outColNumber) {
            if (outRowNumber) {
                makeColumn()
                this.append(blank(rfrmt.format(0)))
                this.append(delimiter)
            }
            for (j in acols) {
                makeColumn()
                this.append(cfrmt.format(j))
                this.append(delimiter)
            }
            makeColumn()
            foundCrosses = true
            this.append(line)
            lineSize = this.length - 2
            makeLine(lineSize)
        }
        for (i in arows) {
            if (outRowNumber) {
                makeColumn()
                this.append(rfrmt.format(i))
                this.append(delimiter)
            }
            for (j in acols) {
                makeColumn()
                val org = this@stringify[i, j]
                val data = mult * org
                val valformat = formatter.format(data)
                val resformat = if (outColNumber) cfrmt.format(valformat) else valformat
                val sval = if (skipZeros && data == 0.0) blank(resformat) else resformat
                if (colorize) {
                    val color =
                            if (org.round(precision) == max.round(precision)) ANSI_RED
                            else if (org.round(precision) == min.round(precision)) ANSI_BLUE
                            else ANSI_RESET
                    this.append("$color$sval$ANSI_RESET")
                } else {
                    this.append(sval)
                }
                this.append(delimiter)
            }
            makeColumn()
            if (lineSize == -1)
                lineSize = this.length - 1
            foundCrosses = true
            if (i != rows - 1 || outEdges)
                this.append(line)
            makeLine(lineSize)
        }

        makeBorder(lineSize)

        if (colorize)
            this.insert(0, ANSI_RESET)
    }
}

inline operator fun DoubleMatrix.plus(value: Double): DoubleMatrix = add(value)
inline operator fun DoubleMatrix.plus(other: DoubleMatrix): DoubleMatrix = add(other)
inline operator fun DoubleMatrix.plusAssign(value: Double): Unit { addi(value) }
inline operator fun DoubleMatrix.plusAssign(other: DoubleMatrix): Unit { addi(other) }
inline operator fun DoubleMatrix.minus(value: Double): DoubleMatrix = sub(value)
inline operator fun DoubleMatrix.minus(value: DoubleMatrix): DoubleMatrix = sub(value)
inline operator fun DoubleMatrix.minusAssign(value: Double): Unit { subi(value) }
inline operator fun DoubleMatrix.minusAssign(value: DoubleMatrix): Unit { subi(value) }
inline operator fun DoubleMatrix.times(value: Double): DoubleMatrix = mul(value)
inline operator fun DoubleMatrix.times(value: DoubleMatrix): DoubleMatrix = mmul(value)

infix inline fun DoubleMatrix.forIndices(block: (i: Int, j: Int) -> Unit): DoubleMatrix {
//    arows.forEach { i -> acols.forEach { j -> block(i, j) } }
    for (i in arows) for (j in acols) block(i, j)
    return this
}

infix inline fun DoubleMatrix.forEach(block: (value: Double) -> Unit): DoubleMatrix = forIndices { i, j -> block(this[i, j]) }

infix inline fun DoubleMatrix.transform(block: (value: Double) -> Double): DoubleMatrix {
    val result = DoubleMatrix(rows, columns)
    result.forIndices { i, j -> result[i, j] = block(this[i, j]) }
    return result
}

inline fun DoubleMatrix.fold(initial: Double, block: (value: Double) -> Double): Double {
    var result = initial
    forEach { result += block(it) }
    return result
}

//inline fun DoubleMatrix.filterIndices(condition: (i: Int, j: Int) -> Boolean): Set<Pair<Int, Int>> {
//    val result = HashSet<Pair<Int, Int>>()
//    forIndices { i, j -> if (condition(i, j)) result.add(Pair(i, j)) }
//    return result
//}
//
//inline fun DoubleMatrix.filterIndicesByValue(condition: (value: Double) -> Boolean): Set<Pair<Int, Int>> {
//    return filterIndices { i, j -> condition(this[i, j]) }
//}

//inline fun DoubleMatrix.modifyIfIndexed(
//        condition: (i: Int, j: Int) -> Boolean,
//        block: (i: Int, j: Int) -> Double): DoubleMatrix {
//    arows.forEach { i -> acols.filter { j -> condition(i, j) }.forEach { j -> this[i, j] = block(i, j) } }
//    return this
//}

//inline fun DoubleMatrix.modifyIf(condition: (value: Double) -> Boolean, block: (value: Double) -> Double): DoubleMatrix =
//        modifyIfIndexed({ i, j -> condition(this[i, j]) }, { i, j -> block(this[i, j]) })

//infix inline fun DoubleMatrix.modifyIndexed(block: (i: Int, j: Int) -> Double): DoubleMatrix =
//        forIndices { i, j -> this[i, j] = block(i, j) }

//infix inline fun DoubleMatrix.modify(block: (value: Double) -> Double): DoubleMatrix =
//        modifyIndexed { i, j -> block(this[i, j]) }

inline fun sum(matrix: DoubleMatrix): Double = matrix.sum()
inline fun max(matrix: DoubleMatrix): Double = matrix.max()
inline fun min(matrix: DoubleMatrix): Double = matrix.min()
inline fun diag(matrix: DoubleMatrix): DoubleMatrix = matrix.diag()

inline val DoubleMatrix.asList: List<Double> get() = elementsAsList()

inline fun DoubleMatrix.sum(power: Double): Double = fold(0.0) { Math.pow(it, power) }

inline fun DoubleMatrix.prune(threshold: Double) = forIndices { i, j -> this[i, j] = if (this[i, j] < threshold) 0.0 else this[i, j] }

inline fun DoubleMatrix.normalizeRows(): DoubleMatrix {
    val sums = rowSums()
    return forIndices { i, j -> this[i, j] /= sums[i] }
//    return this modifyIndexed { i, j -> this[i, j] / sums[i] }
}

inline fun DoubleMatrix.normalizeCols(): DoubleMatrix {
    val sums = columnSums()
    return forIndices { i, j -> this[i, j] /= sums[j] }
//    return this modifyIndexed { i, j -> this[i, j] / sums[j] }
}

inline fun DoubleMatrix.forEachRow(block: (row: DoubleMatrix) -> Unit) = arows.forEach { i -> block(row(i)) }
inline fun DoubleMatrix.forEachCol(block: (col: DoubleMatrix) -> Unit) = acols.forEach { j -> block(col(j)) }

inline fun DoubleMatrix.forEachRowIndexed(block: (i: Int, row: DoubleMatrix) -> Unit) = arows.forEach { i -> block(i, row(i)) }
inline fun DoubleMatrix.forEachColIndexed(block: (j: Int, col: DoubleMatrix) -> Unit) = acols.forEach { j -> block(j, col(j)) }

inline fun DoubleMatrix.row(row: Int): DoubleMatrix = getRow(row)
inline fun DoubleMatrix.col(col: Int): DoubleMatrix = getColumn(col)

//operator fun DoubleMatrix.get(row: Int, col: Int): Double = get(row, col)
inline operator fun DoubleMatrix.set(row: Int, col: Int, value: Double): DoubleMatrix = put(row, col, value)

//fun DoubleMatrix.muli(value: DoubleMatrix): DoubleMatrix = muli(value)
inline fun DoubleMatrix.prodi(value: DoubleMatrix): DoubleMatrix = mmuli(value)

inline infix fun DoubleMatrix.mul(value: DoubleMatrix): DoubleMatrix = mul(value)
inline infix fun DoubleMatrix.prod(value: DoubleMatrix): DoubleMatrix = mmul(value)
inline infix fun DoubleMatrix.dot(value: DoubleMatrix): Double = dot(value)

inline fun DoubleMatrix.powi(power: Double): DoubleMatrix = forIndices { i, j -> this[i, j] = Math.pow(this[i, j], power) }
inline infix fun DoubleMatrix.pow(power: Double): DoubleMatrix = dup().powi(power)

inline operator fun Double.times(m: DoubleMatrix): DoubleMatrix = m.mul(this)
inline operator fun Double.div(m: DoubleMatrix): DoubleMatrix = m.rdiv(this)

inline fun Double.diag(size: Int): DoubleMatrix = this * DoubleMatrix.eye(size)

inline val DoubleMatrix.arows: Iterable<Int> get() = (0 until rows)
inline val DoubleMatrix.acols: Iterable<Int> get() = (0 until columns)


inline val DoubleMatrix.asComplexDoubleMatrix: ComplexDoubleMatrix get() = ComplexDoubleMatrix(this, DoubleMatrix(rows, columns))

inline operator fun ComplexDoubleMatrix.plus(value: ComplexDouble): ComplexDoubleMatrix = add(value)
inline operator fun ComplexDoubleMatrix.plus(other: ComplexDoubleMatrix): ComplexDoubleMatrix = add(other)
inline operator fun ComplexDoubleMatrix.plusAssign(value: ComplexDouble): Unit { addi(value) }
inline operator fun ComplexDoubleMatrix.plusAssign(other: ComplexDoubleMatrix): Unit { addi(other) }
inline operator fun ComplexDoubleMatrix.minus(value: ComplexDouble): ComplexDoubleMatrix = sub(value)
inline operator fun ComplexDoubleMatrix.minus(value: ComplexDoubleMatrix): ComplexDoubleMatrix = sub(value)
inline operator fun ComplexDoubleMatrix.minusAssign(value: ComplexDouble): Unit { subi(value) }
inline operator fun ComplexDoubleMatrix.minusAssign(value: ComplexDoubleMatrix): Unit { subi(value) }
inline operator fun ComplexDoubleMatrix.times(value: ComplexDouble): ComplexDoubleMatrix = mul(value)
inline operator fun ComplexDoubleMatrix.times(value: ComplexDoubleMatrix): ComplexDoubleMatrix = mmul(value)

inline operator fun ComplexDoubleMatrix.set(row: Int, col: Int = 0, value: ComplexDouble): ComplexDoubleMatrix = put(row, col, value)

inline operator fun ComplexDouble.plus(value: ComplexDouble): ComplexDouble = add(value)
inline operator fun ComplexDouble.minus(value: ComplexDouble): ComplexDouble = sub(value)
inline operator fun ComplexDouble.times(value: ComplexDouble): ComplexDouble = mul(value)
//inline operator fun ComplexDouble.div(value: ComplexDouble): ComplexDouble = div(value)
//inline operator fun ComplexDouble.div(value: Double): ComplexDouble = div(value)
//inline operator fun ComplexDouble.div(value: Int): ComplexDouble = div(value.toDouble())

inline fun ComplexDoubleMatrix.abs(): DoubleMatrix = (real() * real() + imag() * imag()).transform { Math.sqrt(it) }