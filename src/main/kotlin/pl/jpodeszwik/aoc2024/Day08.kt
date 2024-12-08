package pl.jpodeszwik.aoc2024

import pl.jpodeszwik.util.Coords
import pl.jpodeszwik.util.Vector
import pl.jpodeszwik.util.loadFile

private fun part1(map: Map<Coords, Char>, antenas: Map<Char, List<Coords>>) {
    val antinodes = mutableSetOf<Coords>()
    antenas.values.forEach { nodeList ->
        nodeList.forEach { node1 ->
            nodeList.forEach { node2 ->
                if (node1 != node2) {
                    val vector = Vector(node1.x - node2.x, node1.y - node2.y)
                    val vectors = listOf(vector, vector.reversed())
                    vectors.forEach { v ->
                        var next = node1.move(v)
                        if (node2 == next) {
                            next = next.move(v)
                        }
                        if (map.contains(next)) {
                            antinodes.add(next)
                        }
                    }
                }
            }
        }
    }
    println(antinodes.size)
}

private fun part2(map: Map<Coords, Char>, antenas: Map<Char, List<Coords>>) {
    val antinodes = mutableSetOf<Coords>()
    antenas.values.forEach { nodeList ->
        nodeList.forEach { node1 ->
            nodeList.forEach { node2 ->
                if (node1 != node2) {
                    val vector = Vector(node1.x - node2.x, node1.y - node2.y)
                    val vectors = listOf(vector, vector.reversed())
                    vectors.forEach { v ->
                        var next = node1.move(v)
                        while (map.contains(next)) {
                            antinodes.add(next)
                            next = next.move(v)
                        }
                    }
                }
            }
        }
    }
    println(antinodes.size)
}

fun main() {
    val lines = loadFile("/aoc2024/input08")
    val map = mutableMapOf<Coords, Char>()
    val antenas = mutableMapOf<Char, MutableSet<Coords>>()

    lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, char ->
            map[Coords(x, y)] = char
            if (char != '.') {
                if (antenas[char] == null) {
                    antenas[char] = mutableSetOf()
                }
                antenas[char]!!.add(Coords(x, y))
            }
        }
    }

    antenas.mapValues { e -> e.value.toList() }.also {
        part1(map.toMap(), it)
        part2(map.toMap(), it)
    }
}
