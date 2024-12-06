package pl.jpodeszwik.util

class Direction(val x: Int, val y: Int)

val UP = Direction(0, -1)
val DOWN = Direction(0, 1)
val LEFT = Direction(-1, 0)
val RIGHT = Direction(1, 0)
val UP_LEFT = Direction(-1, -1)
val UP_RIGHT = Direction(1, -1)
val DOWN_LEFT = Direction(-1, 1)
val DOWN_RIGHT = Direction(1, 1)
