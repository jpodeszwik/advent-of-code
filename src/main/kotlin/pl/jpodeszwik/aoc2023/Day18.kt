package pl.jpodeszwik.aoc2023

import java.lang.Integer.parseInt
import java.util.*
import kotlin.math.max
import kotlin.math.min

private fun part1(input: List<String>) {
    val digCoords = mutableSetOf<MapCoord>()
    var position = MapCoord(0, 0)

    var minRow = 0
    var maxRow = 0
    var minCol = 0
    var maxCol = 0

    digCoords.add(position)
    input.forEach {
        val parts = it.split(" ")
        val direction = when (parts[0]) {
            "L" -> Direction.LEFT
            "R" -> Direction.RIGHT
            "U" -> Direction.UP
            "D" -> Direction.DOWN
            else -> throw IllegalStateException()
        }
        val distance = parseInt(parts[1])
        for (i in 0..<distance) {
            position = position.inDirection(direction, 1)
            digCoords.add(position)

            maxRow = max(maxRow, position.row)
            minRow = min(minRow, position.row)
            maxCol = max(maxCol, position.col)
            minCol = min(minCol, position.col)
        }
    }

    minRow--
    maxRow++
    minCol--
    maxCol++

    val outside = mutableSetOf<MapCoord>()
    val queue = LinkedList<MapCoord>()
    val startCoord = MapCoord(minRow, minCol)

    queue.add(startCoord)
    outside.add(startCoord)
    while (queue.isNotEmpty()) {
        val current = queue.pop()
        current.neighbours().filter {
            it.second.let { coord ->
                coord.row >= minRow && coord.row <= maxRow && coord.col >= minCol && coord.col <= maxCol
            }
        }.filter {
            !outside.contains(it.second) && !digCoords.contains(it.second)
        }.forEach {
            outside.add(it.second)
            queue.add(it.second)
        }
    }

    var count = 0
    for (row in minRow..maxRow) {
        for (col in minCol..maxCol) {
            val coord = MapCoord(row, col)
            if(!outside.contains(coord) && !digCoords.contains(coord)) {
                count++
            }
        }
    }

    println(digCoords.size + count)
}

fun main() {
    val input = loadFile("/aoc2023/input18")
    part1(input)
}


