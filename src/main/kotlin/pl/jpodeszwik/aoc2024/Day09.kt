package pl.jpodeszwik.aoc2024

import pl.jpodeszwik.util.loadFile

private fun part1(line: List<Long>) {
    val disk = ArrayList<Long?>(line.sum().toInt())
    line.forEachIndexed { i, length ->
        for (j in 0..<length) {
            if (i % 2 == 0) {
                disk.add((i / 2).toLong())
            } else {
                disk.add(null)
            }
        }
    }

    var nullIdx = disk.indexOf(null)
    for (i in disk.size - 1 downTo 0) {
        if (i <= nullIdx) {
            break
        }

        if (disk[i] != null) {
            disk[nullIdx] = disk[i]
            disk[i] = null
        }

        while (disk[nullIdx] != null) {
            nullIdx++
        }
    }

    var sum = 0L
    disk.forEachIndexed { index, v ->
        if (v != null) {
            sum += index * v
        }
    }
    println(sum)
}

private data class File(val pos: Int, val size: Int)

private fun part2(line: List<Long>) {
    val files = mutableMapOf<Int, File>()
    val spaces = mutableListOf<File>()
    var pos = 0
    line.forEachIndexed { i, length ->
        if (i % 2 == 0) {
            files[i / 2] = File(pos, length.toInt())
        } else if (length.toInt() > 0) {
            spaces.add(File(pos, length.toInt()))
        }
        pos += length.toInt()
    }
    val keys = ArrayList(files.keys.sortedDescending())
    keys.forEach {
        val file = files[it]!!
        val spaceIndex = spaces.indexOfFirst { space -> space.size >= file.size }
        if (spaceIndex >= 0 && spaces[spaceIndex].pos < file.pos) {
            val space = spaces[spaceIndex]
            files[it] = File(space.pos, file.size)
            val newSize = space.size - file.size
            if (newSize > 0) {
                spaces[spaceIndex] = File(space.pos + file.size, newSize)
            } else {
                spaces.removeAt(spaceIndex)
            }
        }
    }

    var sum = 0L
    files.forEach { (fileId, file) ->
        for (i in 0..<file.size) {
            sum += fileId * (file.pos + i)
        }
    }

    println(sum)
}

fun main() {
    val line = loadFile("/aoc2024/input09")[0].map { it.digitToInt().toLong() }

    part1(line)
    part2(line)
}
