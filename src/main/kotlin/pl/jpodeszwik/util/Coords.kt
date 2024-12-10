package pl.jpodeszwik.util;

data class Coords(val x: Int, val y: Int) {
    fun move(vector: Vector): Coords = Coords(x + vector.x, y + vector.y)

    fun north() = Coords(x, y - 1)
    fun south() = Coords(x, y + 1)
    fun west() = Coords(x - 1, y)
    fun east() = Coords(x + 1, y)

    fun neighbours() = listOf(north(), south(), west(), east())
}
