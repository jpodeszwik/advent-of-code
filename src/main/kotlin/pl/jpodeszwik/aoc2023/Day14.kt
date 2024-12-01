package pl.jpodeszwik.aoc2023

import pl.jpodeszwik.util.loadFile
import java.util.*

private fun part1(lines: List<String>) {
    val counters = IntArray(lines.size)

    lines[0].indices.forEach { col ->
        var top = 0
        lines.indices.forEach { row ->
            val sign = lines[row][col]
            if (sign == '#') {
                top = row + 1
            } else if (sign == 'O') {
                counters[top]++
                top++
            }
        }
    }

    var load = 0L
    counters.forEachIndexed { index, i ->
        load += i * (lines.size - index)
    }

    println(load)
}

private fun bitsetsOfChar(lines: List<String>, char: Char): MutableList<BitSet> {
    return lines.map { line ->
        BitSet(line.length).also { bitSet ->
            line.forEachIndexed { idx, v ->
                bitSet[idx] = v == char
            }
        }
    }.toMutableList()
}

private fun tiltNorth(bitSets: MutableList<BitSet>, hashes: MutableList<BitSet>, columns: Int) {
    for (col in 0..columns) {
        var top = 0
        bitSets.indices.forEach { row ->
            if (bitSets[row][col]) {
                bitSets[row][col] = false
                bitSets[top][col] = true
                top++
            } else if (hashes[row][col]) {
                top = row + 1
            }
        }
    }
}

private fun tiltSouth(bitSets: MutableList<BitSet>, hashes: MutableList<BitSet>, columns: Int) {
    tiltNorth(bitSets.asReversed(), hashes.asReversed(), columns)
}

private fun tiltWest(bitSets: MutableList<BitSet>, hashes: MutableList<BitSet>, columns: Int) {
    bitSets.indices.forEach { row ->
        var top = 0
        for (col in 0..<columns) {
            if (bitSets[row][col]) {
                bitSets[row][col] = false
                bitSets[row][top] = true
                top++
            } else if (hashes[row][col]) {
                top = col + 1
            }
        }
    }
}

private fun tiltEast(bitSets: MutableList<BitSet>, hashes: MutableList<BitSet>, columns: Int) {
    bitSets.indices.forEach { row ->
        var top = 0
        for (col in 0..<columns) {
            val colIdx = columns - col - 1
            if (bitSets[row][colIdx]) {
                bitSets[row][colIdx] = false
                bitSets[row][columns - top - 1] = true
                top++
            } else if (hashes[row][colIdx]) {
                top = col + 1
            }
        }
    }
}

private fun part2(lines: List<String>) {
    val hashes = bitsetsOfChar(lines, '#')
    val rocks = bitsetsOfChar(lines, 'O')

    val tilts = 1000000000

    val visited = HashMap<List<BitSet>, Int>()
    visited[rocks] = 0

    var prev: List<BitSet> = rocks
    var next: List<BitSet>
    var cycleLength = 0
    var firstInCycle = 0
    for (i in 1..tilts) {
        next = doTilts(prev, hashes, lines[0].length)

        if (visited.contains(next)) {
            cycleLength = i - visited[next]!!
            firstInCycle = visited[next]!!
            break
        }

        visited[next] = i
        prev = next
    }

    val extraTilts = (tilts - firstInCycle) % cycleLength

    val resultingBitset = visited.entries.first { e ->
        e.value == firstInCycle + extraTilts
    }.key

    var load = 0L
    resultingBitset.forEachIndexed { row, bitSet ->
        load += bitSet.cardinality() * (lines.size - row)
    }
    println(load)
}

private fun doTilts(bitsets: List<BitSet>, hashes: MutableList<BitSet>, columns: Int): List<BitSet> {
    val copy = bitsets.map { it.clone() as BitSet }.toMutableList()

    tiltNorth(copy, hashes, columns)
    tiltWest(copy, hashes, columns)
    tiltSouth(copy, hashes, columns)
    tiltEast(copy, hashes, columns)

    return copy
}

fun main() {
    val lines = loadFile("/aoc2023/input14")
    part1(lines)
    part2(lines)
}
