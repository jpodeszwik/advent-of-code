package pl.jpodeszwik.util;

data class Coords(val x: Int, val y: Int) {
    fun move(vector: Vector): Coords = Coords(x + vector.x, y + vector.y)
}
