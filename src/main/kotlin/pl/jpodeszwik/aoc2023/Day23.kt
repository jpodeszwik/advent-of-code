package pl.jpodeszwik.aoc2023

import pl.jpodeszwik.util.loadFile
import java.util.*

private const val SLOPE_UP = '^'
private const val SLOPE_DOWN = 'v'
private const val SLOPE_RIGHT = '>'
private const val SLOPE_LEFT = '<'

private val FORCED_DIRECTION = mapOf(
    SLOPE_UP to Direction.UP,
    SLOPE_DOWN to Direction.DOWN,
    SLOPE_RIGHT to Direction.RIGHT,
    SLOPE_LEFT to Direction.LEFT,
)

data class NodeWithDistance(val node: MapCoord, val distance: Int)
data class QueueElement(val startingNode: MapCoord, val node: MapCoord, val distance: Int)

private fun findNodes(lines: List<String>, startingNode: MapCoord, endingNode: MapCoord): Set<MapCoord> {
    val nodes = HashSet<MapCoord>()

    nodes.add(startingNode)
    nodes.add(endingNode)

    for (row in 1..<lines.size - 1) {
        for (col in 1..<lines[0].length - 1) {
            if (lines[row][col] != '#') {
                val currentNode = MapCoord(row, col)
                val neighbourNodes = currentNode.neighbours()
                    .map { it.second }
                    .filter { lines[it.row][it.col] != '#' }
                if (neighbourNodes.size > 2) {
                    nodes.add(currentNode)
                }
            }
        }
    }

    return nodes
}

private fun buildGraph(lines: List<String>, startingNode: MapCoord, endingNode: MapCoord): Map<MapCoord, List<NodeWithDistance>> {
    val visitedNodes = HashSet<MapCoord>()
    val weightedGraph = findNodes(lines, startingNode, endingNode).associateWith { ArrayList<NodeWithDistance>() }.toMutableMap()

    val queue = LinkedList<QueueElement>()
    queue.add(QueueElement(startingNode, startingNode, 0))
    visitedNodes.add(startingNode)
    while (queue.isNotEmpty()) {
        val queueElement = queue.pop()
        if (weightedGraph.containsKey(queueElement.node)) {
            queueElement.node.neighbours()
                .filter { it.second.let { lines[it.row][it.col] != '#' } }
                .filter { !visitedNodes.contains(it.second) }
                .filter { lines[it.second.row][it.second.col] == '.' || FORCED_DIRECTION[lines[it.second.row][it.second.col]] == it.first }
                .forEach {
                    queue.add(QueueElement(queueElement.node, it.second, 1))
                    visitedNodes.add(it.second)
                }
        } else {
            queueElement.node.neighbours()
                .map { it.second }
                .filter { lines[it.row][it.col] != '#' }
                .onEach {
                    if (weightedGraph.contains(it) && it != queueElement.startingNode) {
                        weightedGraph[queueElement.startingNode]!!.add(NodeWithDistance(it, queueElement.distance + 1))
                    }
                }
                .filter { !visitedNodes.contains(it) }
                .forEach {
                    queue.add(QueueElement(queueElement.startingNode, it, queueElement.distance + 1))
                    visitedNodes.add(it)
                }
        }
    }
    return weightedGraph
}

private fun part1(lines: List<String>) {
    val startingNode = MapCoord(1, 1)
    val endingNode = MapCoord(lines.size - 2, lines[0].length - 2)

    val weightedGraph = buildGraph(lines, startingNode, endingNode)

    val dist = mutableMapOf<MapCoord, Int>()
    weightedGraph.keys.forEach {
        dist[it] = Integer.MAX_VALUE
    }

    dist[startingNode] = 0
    val queue = PriorityQueue<MapCoord>(Comparator.comparing { dist[it]!! })

    queue.addAll(weightedGraph.keys)
    while (queue.isNotEmpty()) {
        val node = queue.poll()

        weightedGraph[node]!!.filter { queue.contains(it.node) }.forEach {
            val alt = dist[node]!! - it.distance
            if (alt < dist[it.node]!!) {
                dist[it.node] = alt
                queue.remove(it.node)
                queue.add(it.node)
            }
        }
    }
    println(-dist[endingNode]!!)
}

private fun longestPath(
    graph: Map<MapCoord, List<NodeWithDistance>>,
    visitedNodes: Set<MapCoord>,
    currentNode: MapCoord,
    endingNode: MapCoord
): Long? {
    if (currentNode == endingNode) {
        return 0
    }

    val neighbours = graph[currentNode]!!.filter { !visitedNodes.contains(it.node) }
    if (neighbours.isEmpty()) {
        return null
    }

    return neighbours.mapNotNull {
        val copy = HashSet(visitedNodes)
        copy.add(it.node)
        val pathValue = longestPath(graph, copy, it.node, endingNode)
        if (pathValue == null) null else pathValue + it.distance
    }.maxOrNull()
}

private fun part2(lines: List<String>) {
    val startingNode = MapCoord(1, 1)
    val endingNode = MapCoord(lines.size - 2, lines[0].length - 2)
    val graph = buildGraph2(lines, startingNode, endingNode)
    val result = longestPath(graph, setOf(startingNode), startingNode, endingNode)!!

    println(result)
}

private fun buildGraph2(lines: List<String>, startingNode: MapCoord, endingNode: MapCoord): Map<MapCoord, List<NodeWithDistance>> {
    val initialGraph = buildGraph(lines, startingNode, endingNode)
    val bidirectional = initialGraph.keys.associateWith { ArrayList<NodeWithDistance>() }

    initialGraph.forEach { entry ->
        entry.value.forEach { target ->
            bidirectional[entry.key]!!.add(target)
            bidirectional[target.node]!!.add(NodeWithDistance(entry.key, target.distance))
        }
    }

    return bidirectional
}

fun main() {
    val lines = loadFile("/aoc2023/input23")
    val extraLine = "#".repeat(lines[0].length)
    val linesCopy = ArrayList<String>()
    linesCopy.add(extraLine)
    linesCopy.addAll(lines)
    linesCopy.add(extraLine)

    part1(linesCopy)
    part2(linesCopy)
}
