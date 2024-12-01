package pl.jpodeszwik.aoc2023

import pl.jpodeszwik.util.loadFile

private fun solve(line: String): Long {
    val numbers = line.split(" ").map { java.lang.Long.parseLong(it) }
    val diffs = ArrayList<List<Long>>()
    diffs.add(numbers)
    var currentNumbers = numbers
    while (!currentNumbers.all { it == 0L }) {
        val newDiffs = ArrayList<Long>()
        for (i in 0..<currentNumbers.size - 1) {
            newDiffs.add(currentNumbers[i + 1] - currentNumbers[i])
        }
        diffs.add(newDiffs)
        currentNumbers = newDiffs
    }

    var ret = 0L
    diffs.reversed().forEach {
        val last = it.lastOrNull()
        if (last != null) {
            ret += last
        }
    }
    return ret
}

private fun part1(lines: List<String>) {
    val result = lines.sumOf { solve(it) }
    println(result)
}

private fun solve2(line: String): Long {
    val numbers = line.split(" ").map { java.lang.Long.parseLong(it) }
    val diffs = ArrayList<List<Long>>()
    diffs.add(numbers)
    var currentNumbers = numbers
    while (!currentNumbers.all { it == 0L }) {
        val newDiffs = ArrayList<Long>()
        for (i in 0..<currentNumbers.size - 1) {
            newDiffs.add(currentNumbers[i + 1] - currentNumbers[i])
        }
        diffs.add(newDiffs)
        currentNumbers = newDiffs
    }

    var ret = 0L
    diffs.reversed().forEach {
        val first = it.firstOrNull()
        if (first != null) {
            ret = first - ret
        }
    }
    return ret
}

private fun part2(lines: List<String>) {
    val result = lines.sumOf { solve2(it) }
    println(result)
}

fun main() {
    val lines = loadFile("/aoc2023/input9")

    part1(lines)
    part2(lines)
}