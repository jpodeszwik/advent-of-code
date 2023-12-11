package pl.jpodeszwik.aoc2023

import java.util.*

const val START = 'S'
const val VERTICAL = '|'
const val HORIZONTAL = '-'
const val SOUTH_WEST = '7'
const val SOUTH_EAST = 'F'
const val NORTH_WEST = 'J'
const val NORTH_EAST = 'L'

val possibleOnNorth = setOf(VERTICAL, SOUTH_EAST, SOUTH_WEST)
val possibleOnSouth = setOf(VERTICAL, NORTH_EAST, NORTH_WEST)
val possibleOnWest = setOf(HORIZONTAL, SOUTH_EAST, NORTH_EAST)
val possibleOnEast = setOf(HORIZONTAL, SOUTH_WEST, NORTH_WEST)

data class Coord(val x: Int, val y: Int) {
    fun north() = Coord(x, y - 1)
    fun south() = Coord(x, y + 1)
    fun west() = Coord(x - 1, y)
    fun east() = Coord(x + 1, y)

    fun neighbours() = listOf(north(), south(), west(), east())
}

data class PipeMap(val m: List<String>) {
    fun at(coord: Coord) = m[coord.y][coord.x]
    val startingPoint = findStartingPoint()
    val startingSign = findStartingSign()
    val neighbourMap = buildNeighbourMap()

    private fun findStartingPoint(): Coord {
        m.forEachIndexed { i, line ->
            line.forEachIndexed { j, c ->
                if (c == START) {
                    return Coord(j, i)
                }

            }
        }
        throw IndexOutOfBoundsException()
    }

    private fun findStartingSign(): Char {
        val hasEast = possibleOnEast.contains(at(startingPoint.east()))
        val hasNorth = possibleOnNorth.contains(at(startingPoint.north()))
        val hasSouth = possibleOnSouth.contains(at(startingPoint.south()))

        if (hasNorth) {
            return if (hasSouth) {
                VERTICAL
            } else if (hasEast) {
                NORTH_EAST
            } else {
                NORTH_WEST
            }
        } else if (hasSouth) {
            return if (hasEast) {
                SOUTH_EAST
            } else {
                SOUTH_WEST
            }
        } else {
            return HORIZONTAL
        }
    }

    private fun buildNeighbourMap(): Map<Coord, List<Coord>> {
        val neighbourMap = HashMap<Coord, List<Coord>>()

        m.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                val c2 = if (c == START) startingSign else c
                when (c2) {
                    VERTICAL -> neighbourMap[Coord(x, y)] = listOf(Coord(x, y - 1), Coord(x, y + 1))
                    HORIZONTAL -> neighbourMap[Coord(x, y)] = listOf(Coord(x - 1, y), Coord(x + 1, y))
                    NORTH_EAST -> neighbourMap[Coord(x, y)] = listOf(Coord(x, y - 1), Coord(x + 1, y))
                    NORTH_WEST -> neighbourMap[Coord(x, y)] = listOf(Coord(x, y - 1), Coord(x - 1, y))
                    SOUTH_EAST -> neighbourMap[Coord(x, y)] = listOf(Coord(x, y + 1), Coord(x + 1, y))
                    SOUTH_WEST -> neighbourMap[Coord(x, y)] = listOf(Coord(x, y + 1), Coord(x - 1, y))
                }
            }
        }

        return neighbourMap
    }
}

fun bfs(map: PipeMap): Set<Coord> {
    val visited = HashSet<Coord>()
    val queue = LinkedList<Coord>()
    queue.add(map.startingPoint)

    while (queue.isNotEmpty()) {
        val current = queue.pop()
        visited.add(current)
        val neighbours = map.neighbourMap[current]!!

        queue.addAll(neighbours.filter { !visited.contains(it) })
    }
    return visited
}

private fun part1(map: PipeMap) {
    println(bfs(map).size / 2)
}

private fun stretch(map: PipeMap): PipeMap {
    val stretchMapping = mapOf(
        VERTICAL to listOf("|.", "|."),
        HORIZONTAL to listOf("--", ".."),
        NORTH_WEST to listOf("J.", ".."),
        NORTH_EAST to listOf("L-", ".."),
        SOUTH_WEST to listOf("7.", "|."),
        SOUTH_EAST to listOf("F-", "|."),
    )

    val newLines = ArrayList<String>()
    val extraLine = ".".repeat(map.m[0].length * 2 + 2)
    newLines.add(extraLine)
    map.m.forEach {
        val first = StringBuilder(".")
        val second = StringBuilder(".")
        it.forEach { c ->
            val c2 = if (c == START) map.startingSign else c
            val mapping = stretchMapping.getOrDefault(c2, listOf("..", ".."))
            first.append(c).append(mapping[0][1])
            second.append(mapping[1])
        }
        first.append(".")
        second.append(".")
        newLines.add(first.toString())
        newLines.add(second.toString())
    }
    newLines.add(extraLine)

    return PipeMap(newLines)
}

fun colorGraph(map: PipeMap, printGraph: Boolean): Long {
    val maybePrint = { c: Char ->
        if (printGraph) {
            print(c)
        }
    }

    val red = bfs(map)
    val blue = HashSet<Coord>()
    val queue = LinkedList<Coord>()
    Coord(0, 0).also {
        queue.add(it)
        blue.add(it)
    }

    while (queue.isNotEmpty()) {
        val current = queue.pop()

        val neighbours = current.neighbours().filter {
            it.x >= 0 && it.x < map.m[0].length && it.y >= 0 && it.y < map.m.size
        }.filter { !red.contains(it) && !blue.contains(it) }

        blue.addAll(neighbours)
        queue.addAll(neighbours)
    }

    var greenCount = 0L
    var redCount = 0L
    var blueCount = 0L
    map.m.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (x % 2 == 1 && y % 2 == 1) {
                val coord = Coord(x, y)
                if (red.contains(coord)) {
                    redCount++
                    maybePrint(c)
                } else if (blue.contains(coord)) {
                    blueCount++
                    maybePrint('B')
                } else {
                    greenCount++
                    maybePrint('G')
                }
            }

        }
        if (y % 2 == 1) {
            maybePrint('\n')
        }
    }

    println("red: $redCount, blue: $blueCount, green: $greenCount")

    return greenCount
}

private fun part2(map: PipeMap, printGraph: Boolean = false) {
    val stretchedMap = stretch(map)
    val count = colorGraph(stretchedMap, printGraph)
    println(count)
}

fun main() {
    val map = PipeMap(loadFile("/aoc2023/input10"))
    part1(map)
    part2(map)
}
