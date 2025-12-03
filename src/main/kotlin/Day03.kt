package de.derniklaas

import utils.AoCDay
import utils.splitAndMapToInt

class Day03 : AoCDay() {

    val batteries = input.splitAndMapToInt("")

    override fun part1() = solve(2)

    override fun part2() = solve(12)

    fun solve(searchCount: Int): Long {
        var sum = 0L
        batteries.asSequence().forEach { pack ->
            var output = 0L
            var startIndex = 0

            for (i in 0 until searchCount) {
                val maxSearchIndex = pack.size - (searchCount - i)
                val highestPair = pack.slice(startIndex..maxSearchIndex).withIndex().maxByOrNull { it.value }!!
                output = output * 10 + highestPair.value
                startIndex += highestPair.index + 1
            }
            sum += output
        }
        return sum
    }
}

fun main() {
    Day03().execute()
}