package pl.jpodeszwik.aoc2024

import pl.jpodeszwik.util.loadFile

private class Direction(val x: Int, val y: Int)

private val UP = Direction(0, -1)
private val DOWN = Direction(0, 1)
private val LEFT = Direction(-1, 0)
private val RIGHT = Direction(1, 0)
private val UP_LEFT = Direction(-1, -1)
private val UP_RIGHT = Direction(1, -1)
private val DOWN_LEFT = Direction(-1, 1)
private val DOWN_RIGHT = Direction(1, 1)

private val DIRECTIONS = listOf(UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT)

private class Diagonal(val first: Direction, val second: Direction)

private val DIAGONALS =
    listOf(
        Diagonal(UP_LEFT, DOWN_RIGHT),
        Diagonal(UP_RIGHT, DOWN_LEFT),
    )

private fun part1(lines: List<String>) {
    var result = 0L
    lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c == 'X') {
                DIRECTIONS.forEach { dir ->
                    if (getChar(lines, y + dir.y, x + dir.x) == 'M'
                        && getChar(lines, y + (2 * dir.y), x + (2 * dir.x)) == 'A'
                        && getChar(lines, y + (3 * dir.y), x + (3 * dir.x)) == 'S'
                    ) {
                        result++;
                    }
                }
            }
        }
    }

    println(result)
}

private fun part2(lines: List<String>) {
    var result = 0L
    lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c == 'A' && DIAGONALS.all { dia ->
                    getDiagonalChars(lines, y, x, dia) == setOf('M', 'S')
                }) {
                result++
            }
        }
    }

    println(result)
}

private fun getDiagonalChars(lines: List<String>, y: Int, x: Int, diagonal: Diagonal): Set<Char> {
    return listOf(
        getChar(lines, y + diagonal.first.y, x + diagonal.first.x),
        getChar(lines, y + diagonal.second.y, x + diagonal.second.x),
    ).toSet()
}

private fun getChar(lines: List<String>, y: Int, x: Int): Char {
    if (y > lines.size - 1
        || y < 0
        || x > lines[0].length - 1
        || x < 0
    ) {
        return '.'
    }
    return lines[y][x]
}


fun main() {
    val lines = loadFile("/aoc2024/input04")
    part1(lines)
    part2(lines)
}

