package pl.jpodeszwik.aoc2023

private fun findAnagram(ints: List<Int>, notEqualTo: Int? = null): Int? {
    var biggestAnagram: Int? = null
    var biggestAnagramLength = 0
    for (i in 1..<ints.size) {
        val toLeft = ints.subList(0, i).asReversed()
        val toRight = ints.subList(i, ints.size)

        var shorter: List<Int>
        var longer: List<Int>
        if (toLeft.size > toRight.size) {
            longer = toLeft
            shorter = toRight
        } else {
            longer = toRight
            shorter = toLeft
        }

        if (shorter.indices.all { shorter[it] == longer[it] } && i != notEqualTo) {
            val anagramLength = shorter.size
            if (anagramLength >= biggestAnagramLength) {
                biggestAnagramLength = anagramLength
                biggestAnagram = i
            }
        }
    }

    return biggestAnagram
}

private fun solve1(lines: List<String>): Long {
    val (columns, rows) = parsePicture(lines)

    val columnAnagram = findAnagram(columns)
    val rowAnagram = findAnagram(rows)

    if (columnAnagram != null && rowAnagram != null) {
        throw IllegalStateException()
    }

    return if (rowAnagram != null) {
        rowAnagram.toLong() * 100
    } else {
        columnAnagram!!.toLong()
    }
}

private fun part1(testCases: List<List<String>>) {
    val result = testCases.sumOf(::solve1)
    println("result: $result")
}

private fun solve2(lines: List<String>): Long {
    val (columns, rows) = parsePicture(lines)

    val originalColumn = findAnagram(columns)
    val originalRow = findAnagram(rows)

    for (row in rows.indices) {
        for (col in columns.indices) {
            columns[col] = flipBit(columns[col], row)
            rows[row] = flipBit(rows[row], col)

            val columnAnagram = findAnagram(columns, originalColumn)
            val rowAnagram = findAnagram(rows, originalRow)

            columns[col] = flipBit(columns[col], row)
            rows[row] = flipBit(rows[row], col)

            if (columnAnagram != null && rowAnagram != null) {
                println("col $col row $row")
                println("columnAnagram ${columnAnagram} rowAnagram ${rowAnagram}")
            }

            if (rowAnagram != null) {
                return rowAnagram.toLong() * 100
            } else if (columnAnagram != null) {
                return columnAnagram.toLong()
            }
        }
    }

    throw IllegalStateException()
}

private fun parsePicture(lines: List<String>): Pair<ArrayList<Int>, ArrayList<Int>> {
    val columns = ArrayList<Int>()
    val rows = ArrayList<Int>()

    lines.indices.forEach { row ->
        var rowVal = 0
        lines[0].indices.forEach { col ->
            rowVal += (if (lines[row][col] == '#') 1 else 0) shl col
        }
        rows.add(rowVal)
    }

    lines[0].indices.forEach { col ->
        var colVal = 0
        lines.indices.forEach { row ->
            colVal += (if (lines[row][col] == '#') 1 else 0) shl row
        }
        columns.add(colVal)
    }
    return Pair(columns, rows)
}

private fun part2(testCases: List<List<String>>) {
    val result = testCases.sumOf(::solve2)
    println("result: $result")
}

fun main() {
    val file = loadEntireFile("/aoc2023/input13")

    val testCases = file.split("\n\n")
        .map { it.split("\n") }

    part1(testCases)
    part2(testCases)
}