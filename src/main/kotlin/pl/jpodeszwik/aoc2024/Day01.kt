package pl.jpodeszwik.aoc2024

import pl.jpodeszwik.util.loadFile
import java.lang.Long.parseLong
import kotlin.collections.ArrayList
import kotlin.math.abs

private fun part1(lines: List<String>) {
    var sum = 0L
    val left = ArrayList<Long>()
    val right = ArrayList<Long>()
    lines.forEach {
        val numbers = it.split("\\s+".toRegex()).map(::parseLong)
        left.add(numbers[0])
        right.add(numbers[1])
    }
    left.sort()
    right.sort()

    left.forEachIndexed {
        i, it ->
        sum += abs(it - right[i])
    }
    println(sum)
}

private fun part2(lines: List<String>) {
    val left = ArrayList<Long>()
    val right = HashMap<Long, Long>()
    lines.forEach {
        val numbers = it.split("\\s+".toRegex()).map(::parseLong)
        left.add(numbers[0])
        right.compute(numbers[1]) {
            _, v ->
            if (v == null) 1 else v + 1
        }
    }

    val sum = left.sumOf { v -> v * right.getOrDefault(v, 0) }
    println(sum)
}

fun main() {
    val lines = loadFile("/aoc2024/input01")
    part1(lines)
    part2(lines)
}

