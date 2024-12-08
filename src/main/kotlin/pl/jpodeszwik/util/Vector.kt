package pl.jpodeszwik.util

data class Vector(val x: Int, val y: Int) {
    fun reversed() = Vector(-x, -y)
}

val UP = Vector(0, -1)
val DOWN = Vector(0, 1)
val LEFT = Vector(-1, 0)
val RIGHT = Vector(1, 0)
val UP_LEFT = Vector(-1, -1)
val UP_RIGHT = Vector(1, -1)
val DOWN_LEFT = Vector(-1, 1)
val DOWN_RIGHT = Vector(1, 1)
