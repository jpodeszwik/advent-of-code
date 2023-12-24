package pl.jpodeszwik.aoc2023

import java.util.*

private fun part1(startingPoint: MapCoord, walkableTiles: Set<MapCoord>) {
    val queue = LinkedList<MapCoord>()
    val visited = mutableMapOf<MapCoord, Int>()
    val maxDistance = 64

    visited[startingPoint] = 0
    queue.add(startingPoint)
    while (queue.isNotEmpty()) {
        val node = queue.pop()
        val nodeDistance = visited[node]!!

        if (nodeDistance >= maxDistance) {
            continue
        }

        node.neighbours()
            .filter { walkableTiles.contains(it.second) }
            .forEach {
                val neighbour = it.second
                if (!visited.containsKey(neighbour)) {
                    visited[neighbour] = nodeDistance + 1
                    queue.add(neighbour)
                }
            }
    }

    val result = visited
        .filter { it.value % 2 == 0 && it.value <= maxDistance }
        .count()

    println(result)
}

private fun parseMap(lines: List<String>): Pair<MapCoord, Set<MapCoord>> {
    var startingPoint: MapCoord? = null
    val walkableTiles = mutableSetOf<MapCoord>()

    lines.indices.forEach { row ->
        lines[0].indices.forEach { col ->
            if (lines[row][col] == '.') {
                walkableTiles.add(MapCoord(row, col))
            } else if (lines[row][col] == 'S') {
                startingPoint = MapCoord(row, col)
            }
        }
    }

    return Pair(startingPoint!!, walkableTiles)
}

fun main() {
    val lines = loadFile("/aoc2023/input21")
    val (startingPoint, walkableTiles) = parseMap(lines)
    part1(startingPoint, walkableTiles)
}