package pl.jpodeszwik.aoc2024

import pl.jpodeszwik.util.loadFile

data class Equation(val target: Long, val numbers: List<Long>)

private fun hasValidSolution(target: Long, current: Long, list: List<Long>): Boolean {
    if (list.isEmpty()) {
        return current == target
    }

    val tail = list.subList(1, list.size)
    return hasValidSolution(target, current + list[0], tail)
        || hasValidSolution(target, current * list[0], tail)
}

private fun part1(equations: List<Equation>) {
    val result = equations.filter {
        hasValidSolution(it.target, it.numbers[0], it.numbers.subList(1, it.numbers.size))
    }.sumOf { it.target }
    println(result)
}

private fun hasValidSolutionWithConcat(target: Long, current: Long, list: List<Long>): Boolean {
    if (list.isEmpty()) {
        return current == target
    }

    val tail = list.subList(1, list.size)
    return hasValidSolutionWithConcat(target, current + list[0], tail)
        || hasValidSolutionWithConcat(target, current * list[0], tail)
        || hasValidSolutionWithConcat(target, (current.toString() + list[0].toString()).toLong(), tail)
}

private fun part2(equations: List<Equation>) {
    val result = equations.filter {
        hasValidSolutionWithConcat(it.target, it.numbers[0], it.numbers.subList(1, it.numbers.size))
    }.sumOf { it.target }
    println(result)
}

fun main() {
    val lines = loadFile("/aoc2024/input07")
    val equations = lines.map { line ->
        val parts = line.split(':')
        Equation(parts[0].toLong(), parts[1].split(' ').filter { it.isNotBlank() }.map { it.toLong() })
    }
    part1(equations)
    part2(equations)
}
