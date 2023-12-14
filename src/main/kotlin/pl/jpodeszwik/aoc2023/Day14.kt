package pl.jpodeszwik.aoc2023

private fun part1(lines: List<String>) {
    val counters = IntArray(lines.size)

    lines[0].indices.forEach { col ->
        var top = 0
        lines.indices.forEach { row ->
            val sign = lines[row][col]
            if (sign == '#') {
                top = row + 1
            } else if (sign == 'O') {
                counters[top]++
                top++
            }
        }
    }

    var load = 0L
    counters.forEachIndexed {
        index, i ->
        load += i * (lines.size - index)
    }

    println(load)
}

fun main() {
    val lines = loadFile("/aoc2023/input14")
    part1(lines)
}