package pl.jpodeszwik.aoc2023

import pl.jpodeszwik.util.loadFile
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

const val GALAXY = '#'

data class Universe(val lines: List<String>, val expandedRows: Set<Int>, val expandedCols: Set<Int>, val galaxies: List<Coord>)

private fun parseUniverse(lines: List<String>): Universe {
    val expandedRows = lines.indices.toMutableSet()
    val expandedCols = lines[0].indices.toMutableSet()

    val galaxies = ArrayList<Coord>()
    lines.forEachIndexed { row, s ->
        s.forEachIndexed { col, c ->
            if (c == GALAXY) {
                expandedRows.remove(row)
                expandedCols.remove(col)

                galaxies.add(Coord(col, row))
            }
        }
    }
    return Universe(lines, expandedRows, expandedCols, galaxies)
}

private fun calculateDistance(universe: Universe, age: Int): Long {
    var sum = 0L
    universe.galaxies.forEachIndexed { idx1, coord1 ->
        universe.galaxies.forEachIndexed { idx2, coord2 ->
            if (idx1 < idx2) {
                var distance = (abs(coord1.x - coord2.x) + abs(coord1.y - coord2.y))
                distance += universe.expandedCols.count { it > min(coord1.x, coord2.x) && it < max(coord1.x, coord2.x) } * age
                distance += universe.expandedRows.count { it > min(coord1.y, coord2.y) && it < max(coord1.y, coord2.y) } * age
                sum += distance
            }
        }
    }
    return sum
}

private fun part1(lines: List<String>) {
    val universe = parseUniverse(lines)
    val sum = calculateDistance(universe, 1)
    println(sum)
}

private fun part2(lines: List<String>) {
    val universe = parseUniverse(lines)
    val sum = calculateDistance(universe, 999999)
    println(sum)
}

fun main() {
    val lines = loadFile("/aoc2023/input11")
    part1(lines)
    part2(lines)
}
