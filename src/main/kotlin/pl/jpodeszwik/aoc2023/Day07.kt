package pl.jpodeszwik.aoc2023

import pl.jpodeszwik.util.loadFile

private val cardValues = mapOf(
    'A' to 14,
    'K' to 13,
    'Q' to 12,
    'J' to 11,
    'T' to 10,
    '9' to 9,
    '8' to 8,
    '7' to 7,
    '6' to 6,
    '5' to 5,
    '4' to 4,
    '3' to 3,
    '2' to 2,
)

private fun rank(cards: String): Int {
    val groups = cards.groupBy { it }.map { it.value.size }.sortedDescending()
    if (groups[0] == 3 && groups[1] == 2) {
        return 7
    }
    if (groups[0] == 2 && groups[1] == 2) {
        return 5
    }
    return groups[0] * 2
}

fun compareCards(cards: String, cards2: String, cardValues: Map<Char, Int>): Int {
    for (i in cards.indices) {
        val value = cardValues[cards[i]]!! - cardValues[cards2[i]]!!
        if (value != 0) {
            return value
        }
    }
    println("warning, same cards")
    return 0
}

data class CardSet(val cards: String, val bid: Long, val rank: Int)

private fun part1(lines: List<String>) {
    val sorted = lines.map {
        val parts = it.split(" ")
        CardSet(parts[0], java.lang.Long.parseLong(parts[1]), rank(parts[0]))
    }.sortedWith { a, b ->
        val rankDiff = a.rank - b.rank
        if (rankDiff == 0) compareCards(a.cards, b.cards, cardValues) else rankDiff
    }

    val value = sorted.map { it.bid }
        .reduceIndexed { index, acc, l -> acc + (index + 1).toLong() * l }
    println(value)
}

private val cardValues2 = mapOf(
    'A' to 14,
    'K' to 13,
    'Q' to 12,
    'J' to 1,
    'T' to 10,
    '9' to 9,
    '8' to 8,
    '7' to 7,
    '6' to 6,
    '5' to 5,
    '4' to 4,
    '3' to 3,
    '2' to 2,
)

private fun rank2(cards: String): Int {
    val jokers = cards.filter { 'J' == it }.count()
    if (jokers == 5) {
        return 10
    }

    val groups = cards.filter { 'J' != it }
        .groupBy { it }
        .map { it.value.size }
        .sortedDescending()
        .toMutableList()

    groups[0] += jokers
    if (groups[0] == 3 && groups[1] == 2) {
        return 7
    }
    if (groups[0] == 2 && groups[1] == 2) {
        return 5
    }
    return groups[0] * 2
}

private fun part2(lines: List<String>) {
    val sorted = lines.map {
        val parts = it.split(" ")
        CardSet(parts[0], java.lang.Long.parseLong(parts[1]), rank2(parts[0]))
    }.sortedWith { a, b ->
        val rankDiff = a.rank - b.rank
        if (rankDiff == 0) compareCards(a.cards, b.cards, cardValues2) else rankDiff
    }

    val value = sorted.map { it.bid }
        .reduceIndexed { index, acc, l -> acc + (index + 1).toLong() * l }
    println(value)
}

fun main() {
    val lines = loadFile("/aoc2023/input7")

    part1(lines)
    part2(lines)
}
