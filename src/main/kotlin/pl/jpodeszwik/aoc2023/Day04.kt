package pl.jpodeszwik.aoc2023

import java.lang.Long.parseLong

private fun part1(lines: List<String>) {
    var sum = 0L
    lines.forEach { line ->
        val parts = line.split(":")
        val numbers = parts[1].split("|")

        val winning = numbers[0].split(" ")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .map { parseLong(it) }.toSet()

        val winningCards = numbers[1].split(" ")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .map { parseLong(it) }
            .filter { winning.contains(it) }

        val gameValue = if (winningCards.isEmpty()) 0 else 1L shl winningCards.size - 1
        sum += gameValue
    }
    println(sum)
}

private fun part2(lines: List<String>) {
    val cards = LongArray(lines.size) { _ -> 1 }

    lines.forEachIndexed { index, line ->
        val parts = line.split(":")
        val numbers = parts[1].split("|")

        val card = numbers[0].split(" ")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .map { parseLong(it) }
            .toSet()

        val matchingCards =
            numbers[1].split(" ")
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .map { parseLong(it) }
                .filter { card.contains(it) }
                .size

        for (i in 0..<matchingCards) {
            cards[index + i + 1] += cards[index]
        }
    }

    println(cards.sum())
}

fun main() {
    val lines = loadFile("/aoc2023/input4")
    part1(lines)
    part2(lines)
}