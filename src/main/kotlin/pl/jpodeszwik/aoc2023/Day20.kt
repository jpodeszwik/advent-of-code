package pl.jpodeszwik.aoc2023

import java.util.*

data class Signal(val src: String, val dst: String, val high: Boolean)

abstract class SignalModule {
    abstract fun handleSignal(signal: Signal): List<Signal>
}

data class BroadcastModule(val name: String, val outputs: List<String>) : SignalModule() {
    override fun handleSignal(signal: Signal) = outputs.map { Signal(name, it, signal.high) }
}

data class FlipFlopModule(val name: String, val outputs: List<String>, var high: Boolean = false) : SignalModule() {
    override fun handleSignal(signal: Signal): List<Signal> {
        if (signal.high) {
            return emptyList()
        }

        this.high = !this.high
        return outputs.map { Signal(name, it, this.high) }
    }
}

data class ConjunctionModule(val name: String, val outputs: List<String>, val inputsState: MutableMap<String, Boolean>) : SignalModule() {
    override fun handleSignal(signal: Signal): List<Signal> {
        inputsState[signal.src] = signal.high

        if (inputsState.values.all { it }) {
            return outputs.map { Signal(name, it, false) }
        }

        return outputs.map { Signal(name, it, true) }
    }
}

private fun part1(modules: Map<String, SignalModule>) {
    val signalQueue = LinkedList<Signal>()

    var highCount = 0L
    var lowCount = 0L
    for (i in 1..1000) {
        lowCount++
        signalQueue.add(Signal("", "broadcaster", false))
        while (signalQueue.isNotEmpty()) {
            val signal = signalQueue.pop()
            val outputSignals = modules[signal.dst]?.handleSignal(signal) ?: emptyList()

            outputSignals.forEach {
                if (it.high) {
                    highCount++
                } else {
                    lowCount++
                }
            }
            signalQueue.addAll(outputSignals)
        }
    }

    println("high $highCount, low $lowCount")
    println(highCount * lowCount)
}

fun main() {
    val lines = loadFile("/aoc2023/input20")
    val splitLines = lines.map { it.split(" -> ") }

    val nameToOutputs = splitLines.associate {
        val name = if (it[0] == "broadcaster") it[0] else it[0].substring(1)
        val outputs = it[1].split(", ")
        Pair(name, outputs)
    }

    val modules = HashMap<String, SignalModule>()
    splitLines.forEach {
        val name = if (it[0] == "broadcaster") it[0] else it[0].substring(1)
        if (name == "broadcaster") {
            modules[name] = BroadcastModule(name, nameToOutputs[name]!!)
        } else if (it[0].startsWith("%")) {
            modules[name] = FlipFlopModule(name, nameToOutputs[name]!!)
        } else if (it[0].startsWith("&")) {
            val inputs = nameToOutputs
                .filter { it.value.contains(name) }
                .map { it.key }
            modules[name] = ConjunctionModule(name, nameToOutputs[name]!!, inputs.associateWith { false }.toMutableMap())
        }
    }

    part1(modules)
}
