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

private fun part1(graph: Map<MapCoord, Int>) {
    val dist = mutableMapOf<MapCoord, Cost>()
    graph.keys.forEach {
        dist[it] = Cost(mutableMapOf())
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
    queue.addAll(graph.keys)

    while (queue.isNotEmpty()) {
        val current = queue.poll()
        val currentDist = dist[current]!!
        current.neighbours()
            .filter { graph.containsKey(it.second) }
            .forEach { n ->
                val (direction, neighbour) = n
                val neighbourDist = dist[neighbour]!!
                val edgeCost = graph[neighbour]!!

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

private fun part2(graph: Map<MapCoord, Int>) {
    val dist = mutableMapOf<MapCoord, Cost>()

    graph.keys.forEach {
        dist[it] = Cost(mutableMapOf())
    }

    val initial = MapCoord(0, 0)
    dist[initial] = Cost(
        mutableMapOf(
            DirStreak(Direction.DOWN, 0) to 0,
            DirStreak(Direction.RIGHT, 0) to 0,
        )
    )

    val queue = PriorityQueue<MapCoord>(Comparator.comparing { dist[it]!!.value() })
    queue.addAll(graph.keys)

    while (queue.isNotEmpty()) {
        val current = queue.poll()
        val currentDist = dist[current]!!
        current.neighbours()
            .filter { graph.containsKey(it.second) }
            .forEach { n ->
                val (direction, neighbour) = n
                val neighbourDist = dist[neighbour]!!
                val edgeCost = graph[neighbour]!!

                currentDist.costs
                    .filter { direction.opposite() != it.key.direction }
                    .forEach {
                        if (direction == it.key.direction) {
                            val streak = it.key.streak + 1
                            if (streak <= 10) {
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
                        } else {
                            if (it.key.streak >= 4) {
                                val dirStreak = DirStreak(
                                    direction,
                                    1
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
    }

    val maxKey = dist.keys.maxBy { it.row + it.col }
    println(dist[maxKey]!!.value())
}

private fun buildGraph(input: List<String>): MutableMap<MapCoord, Int> {
    val coords = mutableMapOf<MapCoord, Int>()
    input.forEachIndexed { rowNum, row ->
        row.forEachIndexed { colNum, v ->
            val coord = MapCoord(rowNum, colNum)
            coords[coord] = parseInt(v.toString())

        }
    }
    return coords
}

fun main() {
    val input = loadFile("/aoc2023/input17")
    val graph = buildGraph(input)
    part1(graph)
    part2(graph)
}
