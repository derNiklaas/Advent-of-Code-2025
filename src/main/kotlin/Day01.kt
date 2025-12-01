package de.derniklaas

import utils.AoCDay

class Day01 : AoCDay() {

    val instructions = input.map { line ->
        val direction = if (line[0] == 'R') 1 else -1
        val amount = line.substring(1).toInt()
        direction to amount
    }

    override fun part1(): Any {
        return runDial()
    }

    override fun part2(): Any {
        return runDial(true)
    }

    fun runDial(partB: Boolean = false): Int {
        return sequence {
            for (instruction in instructions) {
                if (partB) {
                    repeat(instruction.second) {
                        yield(instruction.first)
                    }
                } else {
                    yield(instruction.first * instruction.second)
                }
            }
        }.runningFold(50, Math::addExact)
            .count { it % 100 == 0 }
    }
}

fun main() {
    Day01().execute()
}