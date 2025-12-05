package de.derniklaas

import utils.AoCDay
import utils.Vec2D
import utils.toPointMap

class Day04 : AoCDay() {

    val map = input.toPointMap().filter { it.value == '@' }.keys

    override fun part1() = solve()

    override fun part2() = solve(true)

    fun solve(partB: Boolean = false): Int {
        var available = 0
        var lastSize: Int
        val copySet = map.toMutableSet()

        while (true) {
            lastSize = copySet.size
            val toDelete = mutableListOf<Vec2D>()

            copySet.forEach { location ->
                val neighbours = location.getNeighbours()
                if (neighbours.count { it in copySet } < 4) {
                    available++
                    toDelete += location
                }
            }

            toDelete.forEach { copySet.remove(it) }
            if (copySet.size == lastSize || !partB) break
        }
        return available
    }
}

fun main() {
    Day04().execute()
}
