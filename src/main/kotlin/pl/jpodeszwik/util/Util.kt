package pl.jpodeszwik.util

import kotlin.system.measureTimeMillis

class Util {
}

fun loadEntireFile(name: String) = Util::class.java.getResource(name)!!.readText()

fun loadFile(name: String) = loadEntireFile(name).split("\n")

fun flipBit(num: Int, bit: Int): Int {
    val lowestBit = (num shr bit) and 1

    return if (lowestBit == 1) {
        num - (1 shl bit)
    } else {
        num + (1 shl bit)
    }
}

fun measureTimeSeconds(block: () -> Unit) = measureTimeMillis { block() }.also { println("time: ${it / 1000}s") }
