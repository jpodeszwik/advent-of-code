package pl.jpodeszwik.aoc2023

import pl.jpodeszwik.util.loadEntireFile
import java.lang.Integer.parseInt
import java.util.*

data class Condition(val variable: Char, val operation: Char, val value: Int) {
    fun isSatisfiedBy(systemPart: SystemPart): Boolean {
        val varValue = systemPart.values[variable]!!
        return when (operation) {
            '>' -> varValue > value
            '<' -> varValue < value
            else -> throw IllegalStateException()
        }
    }
}

data class Rule(val condition: Condition?, val target: String) {
    fun isMatched(systemPart: SystemPart): Boolean {
        return condition == null || condition.isSatisfiedBy(systemPart)
    }
}

data class SystemPart(val values: Map<Char, Int>)

data class ValueRange(val min: Int, val max: Int) {
    fun split(condition: Condition): Pair<ValueRange?, ValueRange?> {
        if (condition.operation == '>') {
            if (max <= condition.value) {
                return Pair(null, this)
            }

            if (min > condition.value) {
                return Pair(this, null)
            }

            return Pair(ValueRange(condition.value + 1, max), ValueRange(min, condition.value))
        }

        if (min >= condition.value) {
            return Pair(null, this)
        }

        if (max < condition.value) {
            return Pair(this, null)
        }

        return Pair(ValueRange(min, condition.value - 1), ValueRange(condition.value, max))
    }

    fun size(): Int {
        return max - min + 1
    }
}

data class SystemPartValueRanges(val values: Map<Char, ValueRange>) {
    fun splitOnCondition(condition: Condition?): Pair<SystemPartValueRanges?, SystemPartValueRanges?> {
        if (condition == null) {
            return Pair(this, null)
        }

        val variable = condition.variable
        val valueRange = this.values[variable]!!

        val pair = valueRange.split(condition)
        val matchingRanges = if (pair.first != null) {
            val newValues = HashMap(values)
            newValues[variable] = pair.first!!
            SystemPartValueRanges(newValues)
        } else null

        val nonMatchingRanges = if (pair.second != null) {
            val newValues = HashMap(values)
            newValues[variable] = pair.second!!
            SystemPartValueRanges(newValues)
        } else null

        return Pair(matchingRanges, nonMatchingRanges)
    }
}

private fun parseRule(ruleLine: String): List<Rule> {
    return ruleLine.split(",").map {
        val ruleParts = it.split(":", "<", ">")
        if (ruleParts.size == 1) {
            return@map Rule(null, ruleParts[0])
        }
        return@map Rule(Condition(it[0], it[1], parseInt(ruleParts[1])), ruleParts[2])
    }
}

private fun parseRules(ruleLines: List<String>): Map<String, List<Rule>> {
    val m = mutableMapOf<String, List<Rule>>()
    ruleLines.forEach {
        val parts = it.split("{", "}")
        m[parts[0]] = parseRule(parts[1])
    }

    return m
}

private fun parseSystemParts(lines: List<String>): List<SystemPart> {
    return lines.map {
        val m = mutableMapOf<Char, Int>()
        it.split("{", ",", "}")
            .filter { it.isNotBlank() }
            .forEach {
                val parts = it.split("=")
                m[parts[0][0]] = parseInt(parts[1])
            }
        return@map SystemPart(m)
    }
}

private fun isAccepted(part: SystemPart, rules: Map<String, List<Rule>>): Boolean {
    var currentStep = "in"
    while (currentStep != "R" && currentStep != "A") {
        currentStep = rules[currentStep]!!.find { it.isMatched(part) }!!.target
    }
    return currentStep == "A"
}

private fun part1(rules: Map<String, List<Rule>>, parts: List<SystemPart>) {
    val result = parts.filter { isAccepted(it, rules) }.sumOf { it.values.values.sum() }
    println(result)
}

private fun part2(rules: Map<String, List<Rule>>) {
    val queue = LinkedList<Pair<String, SystemPartValueRanges>>()
    queue.add(
        Pair(
            "in", SystemPartValueRanges(
                mapOf(
                    'x' to ValueRange(1, 4000),
                    'm' to ValueRange(1, 4000),
                    'a' to ValueRange(1, 4000),
                    's' to ValueRange(1, 4000),
                )
            )
        )
    )

    val winningRanges = mutableListOf<SystemPartValueRanges>()
    while (queue.isNotEmpty()) {
        val current = queue.pop()
        val currentRules = rules[current.first]!!
        var ranges = current.second
        for (rule in currentRules) {
            val splitOnCondition = ranges.splitOnCondition(rule.condition)
            if (splitOnCondition.first != null) {
                if (rule.target == "A") {
                    winningRanges.add(splitOnCondition.first!!)
                } else if (rule.target != "R") {
                    queue.add(Pair(rule.target, splitOnCondition.first!!))
                }
            }

            if (splitOnCondition.second == null) {
                break
            }
            ranges = splitOnCondition.second!!
        }
    }

    var sum = 0L
    winningRanges.forEach {
        var mul = 1L
        it.values.values.forEach {
            mul *= it.size()
        }
        sum += mul
    }

    println(sum)
}

fun main() {
    val input = loadEntireFile("/aoc2023/input19")
    val parts = input.split("\n\n").map { it.split("\n") }
    part1(parseRules(parts[0]), parseSystemParts(parts[1]))
    part2(parseRules(parts[0]))
}
