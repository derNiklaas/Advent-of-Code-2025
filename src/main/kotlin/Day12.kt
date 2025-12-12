package de.derniklaas

import utils.*

class Day12 : AoCDay() {

    val parts = input.joinToString("\n").split("\n\n")
    val numberOfHashes = parts.dropLast(1).map { it.count { c -> c == '#' } }

    override fun part1(): Any {
        return parts.last().lines().count { p ->
            val (size, quantities) = p.split(": ")
            size.splitAndMapToInt("x").product() >= quantities.splitAndMapToInt(" ")
                .sumOfIndexed { idx, v -> v * numberOfHashes[idx] }
        }
    }

    override fun part2(): Any {
        return "Very Hard Problem. Cannot be solved by humans yet."
    }
}

fun main() {
    Day12().execute()
}
