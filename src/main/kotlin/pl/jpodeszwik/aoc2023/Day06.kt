package pl.jpodeszwik.aoc2023

import pl.jpodeszwik.util.loadFile
import java.lang.Long.parseLong

private fun parseLine(line: String) = line.split(":")[1]
    .trim()
    .replace("\\s+".toRegex(), " ")
    .split(" ")
    .map { parseLong(it) }

private fun part1(lines: List<String>) {
    val times = parseLine(lines[0])
    val distances = parseLine(lines[1])

    var mul = 1L
    for (i in times.indices) {
        val time = times[i]
        val distance = distances[i]

        var waysToBeat = 0L
        for (j in 1..<time) {
            if ((time - j) * j > distance) {
                waysToBeat++
            }
        }
        mul *= waysToBeat
    }

    println(mul)
}

private fun parseLinePart2(line: String) = line.split(":")[1]
    .trim()
    .replace("\\s+".toRegex(), "")
    .let { parseLong(it) }

private fun part2(lines: List<String>) {
    val time = parseLinePart2(lines[0])
    val distance = parseLinePart2(lines[1])

    var waysToBeat = 0L
    for (j in 1..<time) {
        val resultDistance = (time - j) * j
        if (resultDistance > distance) {
            waysToBeat++
        }
    }

    println(waysToBeat)
}

fun main() {
    val lines = loadFile("/aoc2023/input6")
    part1(lines)
    part2(lines)
}