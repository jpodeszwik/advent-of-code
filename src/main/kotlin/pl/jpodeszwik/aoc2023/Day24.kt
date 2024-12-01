package pl.jpodeszwik.aoc2023

import pl.jpodeszwik.util.loadFile
import java.lang.Long.parseLong
import java.math.BigDecimal
import java.math.MathContext

val MC: MathContext = MathContext.DECIMAL128

data class XYZ(val x: Long, val y: Long, val z: Long)

data class Hailstone(val position: XYZ, val velocity: XYZ)

private fun getAB(hailstone: Hailstone): Pair<BigDecimal, BigDecimal> {
    val a = hailstone.velocity.let {
        BigDecimal.valueOf(it.y).divide(BigDecimal.valueOf(it.x), MC)
    }
    val b = hailstone.position.let {
        BigDecimal.valueOf(it.y).subtract(BigDecimal.valueOf(it.x).multiply(a, MC), MC)
    }
    return Pair(a, b)
}

private fun crossingPoint(hailstone1: Hailstone, hailstone2: Hailstone): Pair<BigDecimal, BigDecimal>? {
    val (a1, b1) = getAB(hailstone1)
    val (a2, b2) = getAB(hailstone2)
    if (a1.compareTo(a2) == 0) {
        return null
    }

    val x = b1.subtract(b2, MC).divide(a1.subtract(a2, MC), MC).negate()
    val y = a1.multiply(x, MC).add(b1, MC)

    return Pair(x, y)
}

private fun isBefore(x: BigDecimal, hs: Hailstone): Boolean =
    if (hs.velocity.x > 0) {
        x < BigDecimal.valueOf(hs.position.x)
    } else {
        x > BigDecimal.valueOf(hs.position.x)
    }

private fun part1(hailstones: List<Hailstone>) {
    val minXY = BigDecimal.valueOf(200000000000000L)
    val maxXY = BigDecimal.valueOf(400000000000000L)

    var count = 0L
    for (i in hailstones.indices) {
        for (j in 0..<i) {
            val hs1 = hailstones[i]
            val hs2 = hailstones[j]
            val cp = crossingPoint(hs1, hs2) ?: continue
            val (x, y) = cp

            if (x < minXY || x > maxXY || y < minXY || y > maxXY) {
                continue
            }

            if (isBefore(x, hs1) || isBefore(x, hs2)) {
                continue
            }

            count++
        }
    }
    println(count)
}

fun main() {
    val lines = loadFile("/aoc2023/input24")
    val hailstones = lines.map {
        it.split(" @ ").map {
            it.split(", ").map { parseLong(it.trim()) }
        }.map { XYZ(it[0], it[1], it[2]) }
    }.map { Hailstone(it[0], it[1]) }

    part1(hailstones)
}
