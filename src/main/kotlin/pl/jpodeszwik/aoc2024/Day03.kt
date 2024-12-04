package pl.jpodeszwik.aoc2024

import pl.jpodeszwik.util.loadFile

private fun part1(lines: List<String>) {
    var result = 0L
    lines.forEach { line ->
        line.forEachIndexed { i, c ->
            if (c == 'm') {
                val substr = line.substring(i)
                if (substr.startsWith("mul(")) {
                    result += tryMultiply(substr)
                }
            }
        }
    }

    println(result)
}

private fun part2(lines: List<String>) {
    var result = 0L
    var enabled = true
    lines.forEach { line ->
        line.forEachIndexed { i, c ->
            if (c == 'm' && enabled) {
                val substr = line.substring(i)
                if (substr.startsWith("mul(")) {
                    result += tryMultiply(substr)
                }
            } else if (c == 'd') {
                val substr = line.substring(i)
                if (substr.startsWith("do()")) {
                    enabled = true
                } else if (substr.startsWith("don't()")) {
                    enabled = false
                }
            }
        }
    }

    println(result)
}

private fun validateNumber(str: String): Boolean {
    return str.length in 1..3 && str.all { it.isDigit() }
}

private fun tryMultiply(substr: String): Long {
    if (!substr.startsWith("mul(") || !substr.contains(")")) {
        return 0
    }

    val middle = substr.substring(4, substr.indexOf(")"))
    if (!middle.contains(",")) {
        return 0
    }

    val maybeNumbers = middle.split(",")
    if (maybeNumbers.size != 2 || !validateNumber(maybeNumbers[0]) || !validateNumber(maybeNumbers[1])) {
        return 0
    }

    return maybeNumbers[0].toLong() * maybeNumbers[1].toLong()
}

fun main() {
    val lines = loadFile("/aoc2024/input03")
    part1(lines)
    part2(lines)
}

