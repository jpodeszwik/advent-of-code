package pl.jpodeszwik.aoc2023

import pl.jpodeszwik.util.loadFile
import java.lang.Integer.parseInt
import kotlin.math.max
import kotlin.math.min

data class BrickCoord(val x: Int, val y: Int, val z: Int)

data class Brick(val first: BrickCoord, val second: BrickCoord)

private fun xyBlocks(brick: Brick): Set<Coord> {
    val s = HashSet<Coord>()
    for (x in brick.first.x..brick.second.x) {
        for (y in brick.first.y..brick.second.y) {
            s.add(Coord(x, y))
        }
    }
    return s
}

private fun groupByFloor(movedBricks: MutableList<Brick>): HashMap<Int, MutableSet<Brick>> {
    val bricksByFloor = movedBricks.fold(HashMap<Int, MutableSet<Brick>>()) { acc, it ->
        if (!acc.containsKey(it.first.z)) {
            acc.put(it.first.z, HashSet())
        }
        if (!acc.containsKey(it.second.z)) {
            acc.put(it.second.z, HashSet())
        }
        acc[it.first.z]!!.add(it)
        acc[it.second.z]!!.add(it)
        acc
    }
    return bricksByFloor
}

private fun moveBricks(bricks: List<Brick>): MutableList<Brick> {
    val sortedBricks = bricks.sortedBy { min(it.first.z, it.second.z) }

    val movedBricks = mutableListOf<Brick>()
    sortedBricks.forEach {
        val itBlocks = xyBlocks(it)
        val newZ = movedBricks.reversed()
            .filter { xyBlocks(it).any { itBlocks.contains(it) } }
            .map { max(it.first.z, it.second.z) + 1 }
            .maxOrNull() ?: 1

        val zDiff = min(it.first.z, it.second.z) - newZ

        movedBricks.add(Brick(
            it.first.let { BrickCoord(it.x, it.y, it.z - zDiff) },
            it.second.let { BrickCoord(it.x, it.y, it.z - zDiff) }
        ))
    }
    return movedBricks
}

private fun part1(bricks: List<Brick>) {
    val movedBricks = moveBricks(bricks)
    val bricksByFloor = groupByFloor(movedBricks)

    val result = movedBricks.filter {
        val itBlocks = xyBlocks(it)
        val itMaxZ = max(it.first.z, it.second.z)
        val bricksToCheck = bricksByFloor.getOrDefault(itMaxZ + 1, emptySet())
            .filter { xyBlocks(it).any { itBlocks.contains(it) } }

        bricksToCheck.all {
            val it2Blocks = xyBlocks(it)
            bricksByFloor[itMaxZ]!!.filter { xyBlocks(it).any { it2Blocks.contains(it) } }.size > 1
        }
    }.count()
    println(result)
}

private fun part2(bricks: List<Brick>) {
    val movedBricks = moveBricks(bricks)
    val bricksByFloor = groupByFloor(movedBricks)

    val holders = HashMap<Brick, Set<Brick>>()
    movedBricks.forEach {
        val itMinZ = min(it.first.z, it.second.z)
        val itBlocks = xyBlocks(it)
        holders[it] = bricksByFloor.getOrDefault(itMinZ - 1, emptySet())
            .filter { xyBlocks(it).any { itBlocks.contains(it) } }.toSet()
    }
    val sortedBricks = movedBricks.sortedBy { min(it.first.z, it.second.z) }

    val result = movedBricks.sumOf {
        val removedBricks = HashSet<Brick>()
        removedBricks.add(it)
        val itMaxZ = max(it.first.z, it.second.z)
        sortedBricks
            .filter { itMaxZ < min(it.first.z, it.second.z) }
            .forEach {
                if (removedBricks.containsAll(holders[it]!!)) {
                    removedBricks.add(it)
                }
            }

        removedBricks.size - 1
    }

    println(result)
}

fun main() {
    val lines = loadFile("/aoc2023/input22")
    val bricks = lines.map {
        it.split("~")
            .map {
                it.split(",")
                    .map { parseInt(it) }
            }.map { BrickCoord(it[0], it[1], it[2]) }
    }.map { Brick(it[0], it[1]) }

    part1(bricks)
    part2(bricks)
}
