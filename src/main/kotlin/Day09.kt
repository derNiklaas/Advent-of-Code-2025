package de.derniklaas

import de.derniklaas.utils.Rectangle
import utils.AoCDay
import utils.Vec2D
import utils.splitAndMapToInt

class Day09 : AoCDay() {

    val points = input.map { it.splitAndMapToInt(",") }.map { Vec2D(it[0], it[1]) }
    val distances = buildList {
        for (i in points.indices) {
            for (j in i + 1 until points.size) {
                add(Rectangle(points[i], points[j]).reorder())
            }
        }
    }.sortedByDescending { it.area }

    override fun part1() = distances.first().area

    override fun part2(): Any {
        val lines = (points + points.first()).windowed(2) { (a, b) -> (a..b).reorder() }
        return distances.first { rect ->
            lines.none { rect.intersects(it) }
        }.area
    }
}

fun main() {
    Day09().execute()
}
