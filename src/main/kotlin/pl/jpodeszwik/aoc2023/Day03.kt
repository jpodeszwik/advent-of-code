package pl.jpodeszwik.aoc2023

import pl.jpodeszwik.util.loadFile
import java.lang.Long.parseLong

fun isAdjacentToNonDigit(lines: List<String>, line: Int, begin: Int, end: Int): Boolean {
    val isSymbol: (Char) -> Boolean = { c -> c != '.' }
    for (i in begin - 1..end + 1) {
        if (line - 1 >= 0) {
            if (i >= 0 && i <= lines[line].length - 1) {
                if (isSymbol(lines[line - 1][i])) {
                    return true
                }
            }
        }

        if (line < lines.size - 1) {
            if (i >= 0 && i <= lines[line].length - 1) {
                if (isSymbol(lines[line + 1][i])) {
                    return true
                }
            }
        }
    }
    if (begin > 0 && isSymbol(lines[line][begin - 1])) {
        return true
    }
    if (end < lines[line].length - 1 && isSymbol(lines[line][end + 1])) {
        return true
    }

    return false
}

private fun part1(lines: List<String>) {
    var sum = 0L
    var digitBegin = -1
    for (i in lines.indices) {
        if (digitBegin != -1) {
            val number = parseLong(lines[i - 1].substring(digitBegin, lines[0].length))
            if (isAdjacentToNonDigit(lines, i - 1, digitBegin, lines[0].length - 1)) {
                sum += number
            }
            digitBegin = -1
        }
        for (j in lines[0].indices) {
            if (lines[i][j].isDigit()) {
                if (digitBegin == -1) {
                    digitBegin = j
                }
            } else {
                if (digitBegin != -1) {
                    val number = parseLong(lines[i].substring(digitBegin, j))
                    if (isAdjacentToNonDigit(lines, i, digitBegin, j - 1)) {
                        sum += number
                    }
                    digitBegin = -1
                }
            }
        }
    }
    if (digitBegin != -1) {
        val number = parseLong(lines[lines.size - 1].substring(digitBegin, lines[lines.size - 1].length))
        if (isAdjacentToNonDigit(lines, lines.size - 1, digitBegin, lines[0].length - 1)) {
            sum += number
        }
    }

    println(sum)
}

fun findAdjacentNumbers(lines: List<String>, row: Int, col: Int): List<Long> {
    assert(lines[row][col] == '*')
    val numbers = ArrayList<Long>()
    if (col > 0 && lines[row][col - 1].isDigit()) {
        numbers.add(parseNumber(lines, row, col - 1))
    }
    if (col < lines[row].length - 1 && lines[row][col + 1].isDigit()) {
        numbers.add(parseNumber(lines, row, col + 1))
    }

    if (row > 0) {
        numbers.addAll(findAdjacentRowNumbers(lines, row - 1, col))
    }

    if (row < lines.size - 1) {
        numbers.addAll(findAdjacentRowNumbers(lines, row + 1, col))
    }

    return numbers
}

fun findAdjacentRowNumbers(lines: List<String>, row: Int, col: Int): List<Long> {
    val line = lines[row]
    if (line[col].isDigit()) {
        return listOf(parseNumber(lines, row, col))
    }

    val numbers = ArrayList<Long>()
    if (col > 0 && line[col - 1].isDigit()) {
        numbers.add(parseNumber(lines, row, col - 1))
    }

    if (col < line.length - 1 && line[col + 1].isDigit()) {
        numbers.add(parseNumber(lines, row, col + 1))
    }

    return numbers
}

fun parseNumber(lines: List<String>, row: Int, col: Int): Long {
    val line = lines[row]
    assert(line[col].isDigit())
    var first = col
    var last = col

    while (first > 0 && line[first - 1].isDigit()) {
        first--
    }

    while (last < line.length - 1 && line[last + 1].isDigit()) {
        last++
    }

    return parseLong(line.substring(first, last + 1))
}

private fun part2(lines: List<String>) {
    var sum = 0L
    for (i in lines.indices) {
        for (j in lines[0].indices) {
            if (lines[i][j] == '*') {
                val numbers = findAdjacentNumbers(lines, i, j)
                if (numbers.size == 2) {
                    sum += numbers[0] * numbers[1]
                }
            }
        }
    }
    println(sum)
}

fun main() {
    val lines = loadFile("/aoc2023/input3")
    part1(lines)
    part2(lines)
}

