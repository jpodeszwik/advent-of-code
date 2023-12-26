package pl.jpodeszwik.aoc2023

import java.lang.Integer.MAX_VALUE
import java.lang.Integer.parseInt
import java.util.*

data class DirStreak(val direction: Direction, val streak: Int)

data class Cost(val costs: MutableMap<DirStreak, Int>) {
    fun newDirStreak(dirStreak: DirStreak, cost: Int): Boolean {
        val currentCost = costs[dirStreak] ?: MAX_VALUE
        if (cost < currentCost) {
            costs[dirStreak] = cost
            return true
        }
        return false
    }

    fun value() = costs.values.minOrNull() ?: MAX_VALUE
}

private fun part1(input: List<String>) {
    val coords = mutableMapOf<MapCoord, Int>()
    val dist = mutableMapOf<MapCoord, Cost>()
    input.forEachIndexed { rowNum, row ->
        row.forEachIndexed { colNum, v ->
            val coord = MapCoord(rowNum, colNum)
            coords[coord] = parseInt(v.toString())
            dist[coord] = Cost(mutableMapOf())
        }
    }

    val initial = MapCoord(0, 0)
    dist[initial] = Cost(
        mutableMapOf(
            DirStreak(Direction.UP, 0) to 0,
            DirStreak(Direction.DOWN, 0) to 0,
            DirStreak(Direction.LEFT, 0) to 0,
            DirStreak(Direction.RIGHT, 0) to 0,
        )
    )

    val queue = PriorityQueue<MapCoord>(Comparator.comparing { dist[it]!!.value() })
    queue.addAll(coords.keys)

    while (queue.isNotEmpty()) {
        val current = queue.poll()
        val currentDist = dist[current]!!
        current.neighbours()
            .filter { coords.containsKey(it.second) }
            .forEach { n ->
                val (direction, neighbour) = n
                val neighbourDist = dist[neighbour]!!
                val edgeCost = coords[neighbour]!!

                currentDist.costs
                    .filter { direction.opposite() != it.key.direction }
                    .forEach {
                        val streak = if (direction == it.key.direction) it.key.streak + 1 else 1
                        if (streak <= 3) {
                            val dirStreak = DirStreak(
                                direction,
                                streak
                            )
                            val updated = neighbourDist.newDirStreak(dirStreak, it.value + edgeCost)
                            if (updated) {
                                queue.remove(neighbour)
                                queue.add(neighbour)
                            }
                        }
                    }
            }
    }

    val maxKey = dist.keys.maxBy { it.row + it.col }
    println(dist[maxKey]!!.value())
}

fun main() {
    val input = loadFile("/aoc2023/input17")
    part1(input)
}
