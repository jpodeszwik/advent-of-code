package pl.jpodeszwik.aoc2023

import java.util.*
import kotlin.math.max

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT,
}

data class NodeWithDirection(val node: MapCoord, val direction: Direction) {
    val row get() = node.row

    val col get() = node.col

    fun up() = NodeWithDirection(node.up(), Direction.UP)
    fun down() = NodeWithDirection(node.down(), Direction.DOWN)
    fun left() = NodeWithDirection(node.left(), Direction.LEFT)
    fun right() = NodeWithDirection(node.right(), Direction.RIGHT)
}

data class MapCoord(val row: Int, val col: Int) {
    fun up() = MapCoord(row - 1, col)
    fun down() = MapCoord(row + 1, col)
    fun left() = MapCoord(row, col - 1)
    fun right() = MapCoord(row, col + 1)

    fun neighbours() = listOf(
        Pair(Direction.UP, up()),
        Pair(Direction.LEFT, left()),
        Pair(Direction.DOWN, down()),
        Pair(Direction.RIGHT, right()),
    )

    fun inDirection(direction: Direction, distance: Int): MapCoord = when (direction) {
        Direction.UP -> MapCoord(row - distance, col)
        Direction.DOWN -> MapCoord(row + distance, col)
        Direction.LEFT -> MapCoord(row, col - distance)
        Direction.RIGHT -> MapCoord(row, col + distance)
    }
}

private fun nextNode(node: NodeWithDirection): NodeWithDirection {
    return when (node.direction) {
        Direction.UP -> node.up()
        Direction.DOWN -> node.down()
        Direction.LEFT -> node.left()
        Direction.RIGHT -> node.right()
    }
}

private fun splitHorizontal(node: NodeWithDirection): List<NodeWithDirection> {
    return when (node.direction) {
        Direction.UP, Direction.DOWN -> listOf(
            node.left(),
            node.right(),
        )

        Direction.LEFT -> listOf(node.left())
        Direction.RIGHT -> listOf(node.right())
    }
}

private fun splitVertical(node: NodeWithDirection): List<NodeWithDirection> {
    return when (node.direction) {
        Direction.UP -> listOf(node.up())
        Direction.DOWN -> listOf(node.down())
        Direction.LEFT, Direction.RIGHT -> listOf(
            node.up(),
            node.down(),
        )
    }
}

private fun mirrorSlash(node: NodeWithDirection): NodeWithDirection {
    return when (node.direction) {
        Direction.UP -> node.right()
        Direction.DOWN -> node.left()
        Direction.LEFT -> node.down()
        Direction.RIGHT -> node.up()
    }
}

private fun mirrorBackSlash(node: NodeWithDirection): NodeWithDirection {
    return when (node.direction) {
        Direction.UP -> node.left()
        Direction.DOWN -> node.right()
        Direction.LEFT -> node.up()
        Direction.RIGHT -> node.down()
    }
}

private fun countEnergized(initialNode: NodeWithDirection, map: List<String>): Int {
    val visitedNodes = HashSet<NodeWithDirection>()
    visitedNodes.add(initialNode)
    val nodeQueue = LinkedList<NodeWithDirection>()
    nodeQueue.add(initialNode)

    val handleNode = { node: NodeWithDirection ->
        if (node.row >= 0 && node.row < map.size && node.col >= 0 && node.col < map[0].length) {
            if (visitedNodes.add(node)) {
                nodeQueue.add(node)
            }
        }
    }

    while (nodeQueue.isNotEmpty()) {
        val node = nodeQueue.pop()
        when (map[node.row][node.col]) {
            '.' -> handleNode(nextNode(node))
            '|' -> splitVertical(node).forEach { handleNode(it) }
            '-' -> splitHorizontal(node).forEach { handleNode(it) }
            '/' -> handleNode(mirrorSlash(node))
            '\\' -> handleNode(mirrorBackSlash(node))
        }
    }

    return visitedNodes.groupBy { it.node }.count()
}

private fun part1(map: List<String>) {
    val initialNode = NodeWithDirection(MapCoord(0, 0), Direction.RIGHT)

    val result = countEnergized(initialNode, map)
    println(result)
}

private fun part2(map: List<String>) {
    val rowMax = map.indices.flatMap { row ->
        listOf(
            countEnergized(NodeWithDirection(MapCoord(row, 0), Direction.RIGHT), map),
            countEnergized(NodeWithDirection(MapCoord(row, map[0].length - 1), Direction.LEFT), map)
        )
    }.max()

    val colMax = map[0].indices.flatMap { col ->
        listOf(
            countEnergized(NodeWithDirection(MapCoord(0, col), Direction.DOWN), map),
            countEnergized(NodeWithDirection(MapCoord(map.size - 1, col), Direction.UP), map)
        )
    }.max()

    println(max(rowMax, colMax))
}

fun main() {
    val input = loadFile("/aoc2023/input16")
    part1(input)
    part2(input)
}


