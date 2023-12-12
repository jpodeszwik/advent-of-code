package pl.jpodeszwik.aoc2023

import java.lang.Integer.parseInt

const val DAMAGED = '#'
const val UNKNOWN = '?'
const val WORKING = '.'

data class ComputationKey(val lineIndex: Int, val numberIndex: Int)

private fun solve(line: String, numbers: List<Int>): Long {
    return solve((line + ".").replace("[.]+".toRegex(), "."), 0, numbers, 0, HashMap())
}

private fun solve(line: String, lineIndex: Int, numbers: List<Int>, numberIndex: Int, cache: MutableMap<ComputationKey, Long>): Long {
    // it would be much simpler to iterate over the indices instead of using recursion
    if (cache.containsKey(ComputationKey(lineIndex, numberIndex))) {
        return cache[ComputationKey(lineIndex, numberIndex)]!!
    }

    if (numberIndex >= numbers.size) {
        return if (IntRange(lineIndex, line.length - 1).all { line[it] != DAMAGED }) {
            1
        } else {
            0
        }
    }

    if (lineIndex >= line.length) {
        return 0
    }

    if (line[lineIndex] == WORKING) {
        return solve(line, lineIndex + 1, numbers, numberIndex, cache).also {
            cache[ComputationKey(lineIndex + 1, numberIndex)] = it
        }
    }

    if (line[lineIndex] == UNKNOWN) {
        val first = numbers[numberIndex]
        if (line.length < first + lineIndex + 1) {
            return 0
        }

        val after = line[first + lineIndex]

        return if (after != DAMAGED && IntRange(0, first - 1).all { line[it + lineIndex] != WORKING }) {
            solve(line, lineIndex + 1, numbers, numberIndex, cache).also {
                cache[ComputationKey(lineIndex + 1, numberIndex)] = it
            } +
            solve(line, first + lineIndex + 1, numbers, numberIndex + 1, cache).also {
                cache[ComputationKey(first + lineIndex + 1, numberIndex + 1)] = it
            }
        } else {
            solve(line, lineIndex + 1, numbers, numberIndex, cache).also {
                cache[ComputationKey(lineIndex + 1, numberIndex)] = it
            }
        }
    }

    if (line[lineIndex] == DAMAGED) {
        val first = numbers[numberIndex]
        if (line.length < first + lineIndex + 1) {
            return 0
        }

        val after = line[first + lineIndex]

        return if (after != DAMAGED && IntRange(0, first - 1).all { line[it + lineIndex] != WORKING }) {
            solve(line, first + lineIndex + 1, numbers, numberIndex + 1, cache).also {
                cache[ComputationKey(first + lineIndex + 1, numberIndex + 1)] = it
            }
        } else {
            0
        }
    }

    return 0
}

private fun part1(lines: List<String>) {
    var sum = 0L
    lines.forEach {
        val parts = it.split(" ")
        val numbers = parts[1].split(",").map { parseInt(it) }
        val value = solve(parts[0], numbers)
        sum += value

    }
    println(sum)
}

private fun part2(lines: List<String>) {
    var sum = 0L
    lines.forEach { it ->
        val parts = it.split(" ")
        val numbers = parts[1].split(",").map { parseInt(it) }
        val line = parts[0]
        var newLine = ""
        val newNumbers = ArrayList<Int>()
        for (i in 0..<5) {
            if (i != 0) {
                newLine += UNKNOWN
            }
            newLine += line
            newNumbers.addAll(numbers)
        }
        val value = solve(newLine, newNumbers)
        sum += value

    }
    println(sum)
}

fun main() {
    val lines = loadFile("/aoc2023/input12")
    part1(lines)
    part2(lines)
}
