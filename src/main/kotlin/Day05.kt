package de.derniklaas

import utils.AoCDay
import utils.chunkedInput
import utils.mapToLong
import utils.splitAndMapToLong

class Day05 : AoCDay() {

    val parts = input.chunkedInput()
    val ranges = parts[0].splitAndMapToLong("-").map { it[0]..it[1] }.sortedBy { it.first }
    val numbers = parts[1].mapToLong()

    override fun part1(): Any {
        return numbers.count { number ->
            ranges.any {
                number in it
            }
        }
    }

    override fun part2(): Long {
        val mergedRanges = mutableListOf<LongRange>()

        for (range in ranges) {
            if (mergedRanges.isEmpty()) {
                mergedRanges += range
            } else {
                val lastRange = mergedRanges.last()
                if (lastRange.last < range.first - 1) {
                    mergedRanges += range
                } else {
                    mergedRanges[mergedRanges.size - 1] = lastRange.first..maxOf(lastRange.last, range.last)
                }
            }
        }

        return mergedRanges.sumOf { it.last - it.first + 1 }
    }
}

fun main() {
    Day05().execute()
}
