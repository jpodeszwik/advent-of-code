package pl.jpodeszwik.aoc2023

import java.lang.Long.parseLong

private val totalAmounts = mapOf(
    "red" to 12L,
    "green" to 13L,
    "blue" to 14L,
)

private fun parseGame(line: String): Pair<Long, Boolean> {
    val parts = line.substring(5).split(": ")
    val gameNumber = parseLong(parts[0])
    val sets = parts[1].split("; ")

    var allSetsMatch = true
    for (set in sets) {
        val setAmounts = mutableMapOf(
            "red" to 0L,
            "green" to 0L,
            "blue" to 0L,
        )
        val colors = set.split(", ")
        for (color in colors) {
            val colorParts = color.split(" ")
            setAmounts[colorParts[1]] = parseLong(colorParts[0])
        }
        val setMatch = setAmounts.all { e ->
            totalAmounts[e.key]!! >= e.value
        }

        allSetsMatch = allSetsMatch && setMatch
    }

    return Pair(gameNumber, allSetsMatch)
}

private fun part1(lines: List<String>) {
    var sum = 0L
    for (line in lines) {
        val (gameNumber, possible) = parseGame(line)
        if (possible) {
            sum += gameNumber
        }
    }
    println(sum)
}

private fun parseGame2(line: String): Long {
    val parts = line.substring(5).split(": ")
    val sets = parts[1].split("; ")

    val minimalAmounts = mutableMapOf(
        "red" to 0L,
        "green" to 0L,
        "blue" to 0L,
    )
    for (set in sets) {
        val colors = set.split(", ")
        for (color in colors) {
            val colorParts = color.split(" ")
            val colorNumber = parseLong(colorParts[0])
            val colorName = colorParts[1]
            if (minimalAmounts[colorName]!! < colorNumber) {
                minimalAmounts[colorName] = colorNumber
            }
        }
    }
    var power = 1L
    for (amount in minimalAmounts) {
        power *= amount.value
    }
    return power
}

private fun part2(lines: List<String>) {
    var sum = 0L
    for (line in lines) {
        val power = parseGame2(line)
        sum += power
    }
    println(sum)
}

fun main() {
    val lines = loadFile("/aoc2023/input2")
    part1(lines)
    part2(lines)
}

