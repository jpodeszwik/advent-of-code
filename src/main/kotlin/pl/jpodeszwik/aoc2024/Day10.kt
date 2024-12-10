package pl.jpodeszwik.aoc2024

import pl.jpodeszwik.util.Coords
import pl.jpodeszwik.util.loadFile

private fun part2(zeros: MutableList<Coords>, map: MutableMap<Coords, Int>, nines: MutableSet<Coords>) {
    val result = zeros.sumOf {
        calculateRoutes2(map, nines, it)
    }

    println(result)
}

fun calculateRoutes2(map: MutableMap<Coords, Int>, nines: MutableSet<Coords>, initial: Coords): Int {
    val visited = mutableMapOf<Coords, Int>()
    val queue = ArrayDeque<Coords>()
    queue.add(initial)
    visited[initial] = 1
    while (queue.isNotEmpty()) {
        val curr = queue.removeFirst()
        val currValue = map[curr]!!
        curr.neighbours()
            .filter { map.contains(it) }
            .filter { map[it] == currValue + 1 }
            .forEach {
                if (visited[it] == null) {
                    visited[it] = 1
                } else {
                    visited[it] = visited[it]!! + 1
                }
                queue.add(it)
            }
    }
    return nines
        .filter { visited.contains(it) }
        .sumOf { visited[it]!! }
}

private fun part1(zeros: MutableList<Coords>, map: MutableMap<Coords, Int>, nines: MutableSet<Coords>) {
    val result = zeros.sumOf {
        calculateRoutes(map, nines, it)
    }

    println(result)
}

fun calculateRoutes(map: MutableMap<Coords, Int>, nines: MutableSet<Coords>, initial: Coords): Int {
    val visited = HashSet<Coords>()
    val queue = ArrayDeque<Coords>()
    queue.add(initial)
    visited.add(initial)
    while (queue.isNotEmpty()) {
        val curr = queue.removeFirst()
        val currValue = map[curr]!!
        curr.neighbours()
            .filter { map.contains(it) }
            .filter { map[it] == currValue + 1 }
            .forEach {
                visited.add(it)
                queue.add(it)
            }
    }
    return nines.filter { visited.contains(it) }.size
}

fun main() {
    val lines = loadFile("/aoc2024/input10")
    val map = mutableMapOf<Coords, Int>()
    val zeros = mutableListOf<Coords>()
    val nines = mutableSetOf<Coords>()

    lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            map[Coords(x, y)] = c.digitToInt()
            if (c.digitToInt() == 0) {
                zeros.add(Coords(x, y))
            } else if (c.digitToInt() == 9) {
                nines.add(Coords(x, y))
            }
        }
    }

    part1(zeros, map, nines)
    part2(zeros, map, nines)
}
