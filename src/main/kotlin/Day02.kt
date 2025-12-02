package de.derniklaas

import utils.AoCDay
import utils.splitAndMapToLong

class Day02 : AoCDay() {

    val idRanges = input[0].split(",").splitAndMapToLong("-").map { it[0]..it[1] }

    override fun part1() = solve()

    override fun part2() = solve(true)

    private fun solve(partB: Boolean = false): Long {
        var sum = 0L
        idRanges.forEach { range ->
            for (id in range) {
                if (partB && isValidPart2(id)) {
                    sum += id
                } else if (!partB && isValidPart1(id)) {
                    sum += id
                }
            }
        }
        return sum
    }

    private fun isValidPart1(number: Long): Boolean {
        val string = number.toString()
        if (string.length % 2 != 0) return false
        val length = string.length.floorDiv(2)
        return string.regionMatches(0, string, length, length, true)
    }

    private fun isValidPart2(number: Long): Boolean {
        val string = number.toString()
        for (chunk in 2..string.length) {
            if (string.length % chunk == 0) {
                val chunkLength = string.length.floorDiv(chunk)
                if (string.take(chunkLength).repeat(chunk) == string) {
                    return true
                }
            }
        }

        return false
    }
}

fun main() {
    Day02().execute()
}

/*
too low:
- 28146997869
- 28146997880


 */
