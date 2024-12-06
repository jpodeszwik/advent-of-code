package pl.jpodeszwik.aoc2024

import pl.jpodeszwik.util.loadFile

private fun checkRules(rules: Map<Int, List<Int>>, update: List<Int>): Boolean {
    val visited = HashSet<Int>()

    for (num in update) {
        val numberRules = rules.getOrDefault(num, listOf())
        visited.add(num)
        if (numberRules.any { visited.contains(it) }) {
            return false
        }
    }

    return true
}

private fun part1(lines: List<String>) {
    val rules = parseRules(lines)
    val updates = parseUpdates(lines)

    val result = updates.filter { checkRules(rules, it) }
        .sumOf { l -> l[l.size / 2] }

    println(result)
}

private fun part2(lines: List<String>) {
    val rules = parseRules(lines)
    val updates = parseUpdates(lines)

    val incorrectUpdates = updates.filterNot { checkRules(rules, it) }
        .map { reorder(rules, it) }
        .sumOf { l -> l[l.size / 2] }

    println(incorrectUpdates)
}


private fun reorder(rules: Map<Int, List<Int>>, update: List<Int>): List<Int> {
    return update.sortedByDescending {
        rules.getOrDefault(it, listOf())
            .filter { n -> update.contains(n) }
            .size
    }
}

private fun parseUpdates(lines: List<String>) = lines.filter { it.contains(',') }
    .map { it.split(',').map { i -> i.toInt() } }

private fun parseRules(lines: List<String>) = lines.filter { it.contains('|') }
    .map { it.split('|').map(String::toInt) }
    .groupBy { it.first() }
    .mapValues { it.value.map { i -> i[1] } }

fun main() {
    val lines = loadFile("/aoc2024/input05")
    part1(lines)
    part2(lines)
}

