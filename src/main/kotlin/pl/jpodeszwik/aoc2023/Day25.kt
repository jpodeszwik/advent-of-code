package pl.jpodeszwik.aoc2023

import pl.jpodeszwik.util.loadFile
import java.util.*

private fun calculateSizes(graph: Map<String, List<String>>, removedEdges: Set<List<String>>) {
    val visited = HashSet<String>()
    val queue = LinkedList<String>()
    val randomNode = graph.keys.random()
    queue.add(randomNode)
    visited.add(randomNode)

    while (queue.isNotEmpty()) {
        val node = queue.pop()
        graph[node]!!.filter { !visited.contains(it) }
            .filter { !removedEdges.contains(listOf(node, it).sorted()) }
            .forEach {
                visited.add(it)
                queue.add(it)
            }
    }

    println(visited.size * (graph.size - visited.size))
}

private fun dijkstra(graph: Map<String, List<String>>, src: String, dst: String, usedNodes: Set<List<String>>): Set<List<String>>? {
    val dist = mutableMapOf<String, Int>()
    val prev = mutableMapOf<String, String>()
    graph.keys.forEach {
        dist[it] = Integer.MAX_VALUE
    }
    dist[src] = 0

    val queue = PriorityQueue<String>(Comparator.comparing { dist[it]!! })
    queue.addAll(graph.keys)

    while (queue.isNotEmpty()) {
        val u = queue.poll()
        if (dist[u] == Integer.MAX_VALUE) {
            break
        }
        graph[u]!!.filter { !usedNodes.contains(listOf(it, u).sorted()) }.forEach {
            val alt = dist[u]!! + 1
            if (alt < dist[it]!!) {
                dist[it] = alt
                prev[it] = u
                queue.remove(it)
                queue.add(it)
            }
        }
    }

    if (dist[dst] == Integer.MAX_VALUE) {
        return null
    }

    var current = dst
    val newUsedNodes = HashSet<List<String>>()
    while (current != src) {
        val prevNode = prev[current]!!
        newUsedNodes.add(listOf(prevNode, current).sorted())
        current = prevNode
    }
    return newUsedNodes
}

private fun tryNDijkstras(
    graph: Map<String, List<String>>,
    src: String,
    dst: String,
    n: Int,
    initialUsedNodes: Set<List<String>> = emptySet()
): Boolean {
    val usedNodes = mutableSetOf<List<String>>()
    usedNodes.addAll(initialUsedNodes)

    for (i in 1..n) {
        val newUsedNodes = dijkstra(graph, src, dst, usedNodes)
        if (newUsedNodes == null) {
            if (i == n) {
                return true
            }
            return false
        }
        usedNodes.addAll(newUsedNodes)
    }
    return false
}

private fun findEdgesToRemove(graph: Map<String, List<String>>, src: String, dst: String): Set<List<String>> {
    val usedNodes = mutableSetOf<List<String>>()

    for (i in 1..3) {
        val newUsedNodes = dijkstra(graph, src, dst, usedNodes)
        usedNodes.addAll(newUsedNodes!!)
    }

    val edgesToRemove = mutableSetOf<List<String>>()
    val usedNodesList = usedNodes.toMutableList()

    for (i in 1..3) {
        for (j in usedNodesList.indices) {
            val usedNode = usedNodesList[j]
            val removedNodes = mutableSetOf<List<String>>()
            removedNodes.addAll(edgesToRemove)
            removedNodes.add(usedNode)
            if (tryNDijkstras(graph, src, dst, 4 - i, removedNodes)) {
                edgesToRemove.add(usedNode)
                usedNodesList.remove(usedNode)
                break
            }
        }
    }

    return edgesToRemove
}

private fun part1(graph: Map<String, List<String>>) {
    val sortedKeys = graph.keys.sorted()

    sortedKeys.indices.forEach { i1 ->
        for (i2 in 0..<i1) {
            val node1 = sortedKeys[i1]
            val node2 = sortedKeys[i2]

            if (tryNDijkstras(graph, node1, node2, 4)) {
                val removedEdges = findEdgesToRemove(graph, node1, node2)
                calculateSizes(graph, removedEdges)
                return
            }
        }
    }
}

fun main() {
    val lines = loadFile("/aoc2023/input25")
    val graph = lines.map { it.split(" ", ": ") }
        .flatten()
        .toSet()
        .associateWith { ArrayList<String>() }

    lines.map { it.split(": ") }
        .forEach {
            it[1].split(" ").forEach { dst ->
                graph[it[0]]!!.add(dst)
                graph[dst]!!.add(it[0])
            }
        }

    part1(graph)
}


