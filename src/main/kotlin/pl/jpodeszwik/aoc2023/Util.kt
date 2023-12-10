package pl.jpodeszwik.aoc2023

import kotlin.system.measureTimeMillis

class Util {
}

fun loadFile(name: String) = Util::class.java.getResource(name)!!.readText().split("\n")

fun measureTimeSeconds(block: () -> Unit) = measureTimeMillis { block() }.also { println("time: ${it / 1000}s") }
