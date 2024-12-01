package pl.jpodeszwik.aoc2023

import pl.jpodeszwik.util.loadFile
import java.lang.Integer.parseInt

private fun hash(input: String): Int {
    var current = 0
    input.forEach { c ->
        current += c.code
        current *= 17
        current %= 256
    }
    return current
}

private fun part1(input: String) {
    val parts = input.split(",")
    val result = parts.sumOf {
        hash(it)

    }
    println(result)
}

data class BoxElement(val key: String, val value: Int)

private fun part2(input: String) {
    val parts = input.split(",")
    val boxes = HashMap<Int, MutableList<BoxElement>>()
    for (i in 0..255) {
        boxes[i] = ArrayList()
    }

    parts.forEach {
        if (it.contains('-')) {
            val key = it.split("-")[0]
            val keyHash = hash(key)
            val boxValues = boxes[keyHash]!!
            val index = boxValues.indexOfFirst { it.key == key }
            if (index != -1) {
                boxValues.removeAt(index)
            }
        } else if (it.contains("=")) {
            val parts2 = it.split("=")
            val key = parts2[0]
            val keyHash = hash(key)
            val value = parseInt(parts2[1])
            val boxValues = boxes[keyHash]!!
            val index = boxValues.indexOfFirst { it.key == key }
            if (index == -1) {
                boxValues.add(BoxElement(key, value))
            } else {
                boxValues[index] = BoxElement(key, value)
            }
        }
    }

    val result = boxes.filter { it.value.isNotEmpty() }.entries
        .sumOf { box -> box.value.indices.sumOf { (box.key + 1) * (it + 1) * box.value[it].value } }

    println(result)
}

fun main() {
    val input = loadFile("/aoc2023/input15")[0]
    part1(input)
    part2(input)
}


