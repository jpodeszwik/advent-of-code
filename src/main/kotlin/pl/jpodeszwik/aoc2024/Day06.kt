package pl.jpodeszwik.aoc2024

import pl.jpodeszwik.util.DOWN
import pl.jpodeszwik.util.Direction
import pl.jpodeszwik.util.LEFT
import pl.jpodeszwik.util.RIGHT
import pl.jpodeszwik.util.UP
import pl.jpodeszwik.util.loadFile

private data class Coords(val x: Int, val y: Int) {
    fun next(direction: Direction) = Coords(x + direction.x, y + direction.y)
}

private val rotateDirections = mapOf(
    UP to RIGHT,
    RIGHT to DOWN,
    DOWN to LEFT,
    LEFT to UP,
)

private fun part2(map: HashMap<Coords, Char>, initialPosition: Coords) {

    var potentialCyclesCount = 0L

    map.keys.forEach {
        if (map[it] == '.') {
            map[it] = '#'
            if (detectCycle(map, initialPosition)) {
                potentialCyclesCount++
            }
            map[it] = '.'

        }
    }
    println(potentialCyclesCount)
}

private fun detectCycle(map: HashMap<Coords, Char>, initialPosition: Coords): Boolean {
    var direction = UP
    var position = initialPosition
    val visited = HashMap<Coords, Direction>()

    while (true) {
        visited[position] = direction

        while (map[position.next(direction)] != '.' && map[position.next(direction)] != null) {
            direction = rotateDirections[direction]!!
        }

        val nextPosition = position.next(direction)
        if (!map.contains(nextPosition)) {
            return false
        }

        position = position.next(direction)
        if (visited[position] == direction) {
            return true
        }
    }
}

private fun part1(map: HashMap<Coords, Char>, initialPosition: Coords) {
    var direction = UP
    var position = initialPosition
    val visited = HashSet<Coords>()
    while (true) {
        visited.add(position)

        while (map[position.next(direction)] != '.' && map[position.next(direction)] != null) {
            direction = rotateDirections[direction]!!
        }

        val nextPosition = position.next(direction)
        if (!map.contains(nextPosition)) {
            break
        }
        position = position.next(direction)
    }
    println(visited.size)
}


fun main() {
    val lines = loadFile("/aoc2024/input06")
    val map = HashMap<Coords, Char>()
    var initialPosition: Coords? = null
    lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c == '^') {
                map[Coords(x, y)] = '.'
                initialPosition = Coords(x, y)
            } else {
                map[Coords(x, y)] = c
            }
        }
    }
    part1(map, initialPosition!!)
    part2(map, initialPosition!!)
}

