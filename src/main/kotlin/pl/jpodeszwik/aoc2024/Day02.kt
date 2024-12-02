package pl.jpodeszwik.aoc2024

import pl.jpodeszwik.util.loadFile
import java.lang.Long.parseLong
import java.lang.Long.signum
import kotlin.math.abs

private fun part1(lines: List<String>) {
    var safe = 0L
    lines.forEach {
        val numbers = it.split("\\s+".toRegex()).map(::parseLong)
        if (checkSafety(numbers)) {
            safe++
        }
    }
    println(safe)
}

fun checkSafety(numbers: List<Long>): Boolean {
    var current = numbers[0]
    val sign = signum(current - numbers[1])
    for (i in 1..<numbers.size) {
        val diff = current - numbers[i]
        if (signum(diff) != sign) {
            return false
        }
        if (abs(diff) > 3 || diff == 0L) {
            return false
        }
        current = numbers[i]
    }
    return true
}

private fun part2(lines: List<String>) {
    var safe = 0L
    lines.forEach {
        val numbers = it.split("\\s+".toRegex()).map(::parseLong)
        if (checkSafety2(numbers)) {
            safe++
        }
    }
    println(safe)
}

fun checkSafety2(numbers: List<Long>): Boolean {
    for (i in numbers.indices) {
        val copy = numbers.filterIndexed { index, _ -> index != i }
        if (checkSafety(copy)) {
            return true
        }
    }
    return false
}

fun main() {
    val lines = loadFile("/aoc2024/input02")
    part1(lines)
    part2(lines)
}

