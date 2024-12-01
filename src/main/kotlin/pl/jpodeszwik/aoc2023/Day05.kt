package pl.jpodeszwik.aoc2023

import pl.jpodeszwik.util.loadFile
import pl.jpodeszwik.util.measureTimeSeconds

data class Range(
    val source: Long,
    val target: Long,
    val length: Long,
)

data class Category(
    val name: String,
    val ranges: MutableList<Range>,
)

fun findLocation(seed: Long, almanac: List<Category>): Long {
    var current = seed
    almanac.forEach { category ->
        val matchingRange = category.ranges.firstOrNull {
            current >= it.source && current < it.source + it.length
        }
        if (matchingRange != null) {
            val target = matchingRange.target + (current - matchingRange.source)
            current = target
        }
    }
    return current
}

fun parseAlmanac(lines: List<String>): List<Category> {
    val almanac = ArrayList<Category>()

    var currentCategory: Category? = null
    lines.forEach {
        if (it.contains("map")) {
            currentCategory = Category(it.split(" ")[0], ArrayList())
            almanac.add(currentCategory!!)
        }

        if (it.isNotEmpty() && it[0].isDigit()) {
            val rangeNumbers = it.split(" ")
                .map { java.lang.Long.parseLong(it) }
            currentCategory!!.ranges.add(Range(rangeNumbers[1], rangeNumbers[0], rangeNumbers[2]))
        }
    }

    return almanac
}

private fun part1(lines: List<String>, seeds: List<Long>) {
    val almanac = parseAlmanac(lines)

    val result = seeds.minOf {
        findLocation(it, almanac)
    }

    println(result)
}

private fun part2(lines: List<String>, seeds: List<Long>) {
    val almanac = parseAlmanac(lines)

    var min = Long.MAX_VALUE
    for (i in 0..<seeds.size / 2) {
        println("range: $i")
        for (j in 0..<seeds[i * 2 + 1]) {
            val loc = findLocation(seeds[i * 2] + j, almanac)
            if (loc < min) {
                min = loc
            }
        }
    }

    println(min)
}

fun main() {
    val lines = loadFile("/aoc2023/input5")

    val seeds = lines[0].split(": ")[1].split(" ")
        .map { java.lang.Long.parseLong(it) }

    part1(lines, seeds)
    measureTimeSeconds { part2(lines, seeds) }
}