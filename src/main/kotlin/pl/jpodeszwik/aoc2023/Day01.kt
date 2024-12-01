package pl.jpodeszwik.aoc2023

import pl.jpodeszwik.util.loadFile
import java.lang.Long.parseLong

private val numbers = mapOf(
    "one" to '1',
    "two" to '2',
    "three" to '3',
    "four" to '4',
    "five" to '5',
    "six" to '6',
    "seven" to '7',
    "eight" to '8',
    "nine" to '9',
)

private fun part1(lines: List<String>) {
    var sum = 0L
    lines.forEach {
        var first = ' '
        var last = ' '
        var str = it
        while (str.isNotEmpty()) {
            if (str[0].isDigit()) {
                last = str[0]
                if (first == ' ') {
                    first = str[0]
                }
            }

            str = str.substring(1)
        }

        sum += parseLong("$first$last")
    }
    println(sum)
}

private fun part2(lines: List<String>) {
    var sum = 0L
    lines.forEach {
        var first = ' '
        var last = ' '
        var str = it
        while (str.isNotEmpty()) {
            if (str[0].isDigit()) {
                last = str[0]
                if (first == ' ') {
                    first = str[0]
                }
            } else {
                for (n in numbers) {
                    if (str.startsWith(n.key)) {
                        last = n.value
                        if (first == ' ') {
                            first = n.value
                        }
                    }
                }
            }

            str = str.substring(1)
        }

        sum += parseLong("$first$last")
    }
    println(sum)
}

fun main() {
    val lines = loadFile("/aoc2023/input1")
    part1(lines)
    part2(lines)
}

