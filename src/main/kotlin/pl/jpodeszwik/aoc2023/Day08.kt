package pl.jpodeszwik.aoc2023

import pl.jpodeszwik.util.loadFile
import pl.jpodeszwik.util.measureTimeSeconds

private fun part1(instructions: String, nodes: Map<String, Pair<String, String>>) {
    var currentNode = "AAA"
    var steps = 0L
    while (currentNode != "ZZZ") {
        for (i in instructions) {
            when (i) {
                'L' -> currentNode = nodes[currentNode]!!.first
                'R' -> currentNode = nodes[currentNode]!!.second
                else -> println("invalid instruction")
            }
            steps++
            if (currentNode == "ZZZ") {
                break
            }
        }
    }
    println(steps)
}

data class NodeData(val node: String, val nodeAfterCycle: String, val zNodesAfter: Set<Long>)

private fun part2(instructions: String, nodes: Map<String, Pair<String, String>>) {
    val nodeDatas = mutableMapOf<String, NodeData>()
    val combinedCycles = 10000
    val cycleLength = combinedCycles * instructions.length

    nodes.keys.map {
        var currentNode = it
        val zAfterNodes = HashSet<Long>()
        if (currentNode.endsWith("Z")) {
            zAfterNodes.add(0)
        }
        for (i in 0..<combinedCycles) {
            instructions.forEachIndexed { idx, instruction ->
                when (instruction) {
                    'L' -> currentNode = nodes[currentNode]!!.first
                    'R' -> currentNode = nodes[currentNode]!!.second
                    else -> println("invalid instruction")
                }
                if (currentNode.endsWith("Z")) {
                    zAfterNodes.add((i * instructions.length) + idx.toLong() + 1)
                }
            }
        }

        NodeData(it, currentNode, zAfterNodes)
    }.forEach {
        nodeDatas[it.node] = it
    }

    println("cycles built")

    var currentNodes = nodes.keys.filter { it.endsWith("A") }
    var steps = 0L
    var fullCycles = 0L
    while (true) {
        val cycleMetadata = currentNodes.map { nodeDatas[it]!! }
        val res = cycleMetadata
            .asSequence()
            .map { it.zNodesAfter }.flatten().groupBy { it }
            .filter { it.value.size == currentNodes.size }
            .map { it.key }
            .minOrNull()

        if (res != null) {
            steps += res.toLong()
            break
        } else {
            steps += cycleLength.toLong()
        }
        currentNodes = cycleMetadata.map { it.nodeAfterCycle }
        fullCycles++
    }
    println(steps)
}

private fun combineNodeDatas(nodeDatas: Map<String, NodeData>, cycleLength: Long): MutableMap<String, NodeData> {
    val newNodeDatas = mutableMapOf<String, NodeData>()

    nodeDatas.values.forEach {
        val nodeDataAfterCycle = nodeDatas[it.nodeAfterCycle]!!
        newNodeDatas[it.node] = NodeData(
            it.node,
            nodeDataAfterCycle.nodeAfterCycle,
            it.zNodesAfter.toSet() + nodeDataAfterCycle.zNodesAfter.map { it2 -> it2 + cycleLength })
    }

    return newNodeDatas
}

private fun part2v2(instructions: String, nodes: MutableMap<String, Pair<String, String>>) {
    var nodeDatas: MutableMap<String, NodeData> = mutableMapOf()
    var cycleLength = instructions.length.toLong()

    nodes.keys.map {
        var currentNode = it
        val zAfterNodes = HashSet<Long>()
        if (currentNode.endsWith("Z")) {
            zAfterNodes.add(0)
        }
        instructions.forEachIndexed { idx, instruction ->
            when (instruction) {
                'L' -> currentNode = nodes[currentNode]!!.first
                'R' -> currentNode = nodes[currentNode]!!.second
                else -> println("invalid instruction")
            }
            if (currentNode.endsWith("Z")) {
                zAfterNodes.add(idx.toLong() + 1)
            }
        }

        NodeData(it, currentNode, zAfterNodes)
    }.forEach {
        nodeDatas[it.node] = it
    }

    for (i in 1..21) {
        nodeDatas = combineNodeDatas(nodeDatas, cycleLength)
        cycleLength *= 2
    }

    println("cycles built")

    var currentNodes = nodes.keys.filter { it.endsWith("A") }
    var steps = 0L
    var fullCycles = 0L
    while (true) {
        val cycleMetadata = currentNodes.map { nodeDatas[it]!! }
        val res = cycleMetadata
            .asSequence()
            .map { it.zNodesAfter }.flatten().groupBy { it }
            .filter { it.value.size == currentNodes.size }
            .map { it.key }
            .minOrNull()

        if (res != null) {
            steps += res.toLong()
            break
        } else {
            steps += cycleLength
        }
        currentNodes = cycleMetadata.map { it.nodeAfterCycle }
        fullCycles++
    }
    println(steps)
}

fun main() {
    val lines = loadFile("/aoc2023/input8")
    val instructions = lines[0]

    val nodes = mutableMapOf<String, Pair<String, String>>()
    for (i in 2..<lines.size) {
        val parts = lines[i].split(" = (", ")")
        val targetNodes = parts[1].split(", ")
        nodes[parts[0]] = Pair(targetNodes[0], targetNodes[1])
    }

    part1(instructions, nodes)
    measureTimeSeconds { part2(instructions, nodes) }
    measureTimeSeconds { part2v2(instructions, nodes) }
}
