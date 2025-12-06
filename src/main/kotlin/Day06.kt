package de.derniklaas

import utils.AoCDay

class Day06 : AoCDay() {
    val maxLength = input.maxOf { it.length }
    val paddedInput = input.map { it.padEnd(maxLength, ' ') }

    val problemSlices = paddedInput.run {
        val spaces = first().indices.filter { index -> all { it[index] == ' ' } }
        listOf(-1) + spaces + listOf(first().length)
    }.zipWithNext { start, end -> start.inc()..end.dec() }

    override fun part1() = solve()

    override fun part2() = solve(true)

    fun solve(partB: Boolean = false): Long {
        val list = buildList {
            problemSlices.map { slice ->
                val rawData = paddedInput.map { it.slice(slice) }
                val dataWidth = rawData.first().length
                val rawInputs = rawData.dropLast(1)
                val operation = Operation.fromSymbol(rawData.last().trim())

                val inputs = if (!partB) {
                    rawInputs
                } else {
                    (dataWidth - 1 downTo 0).map { index -> rawInputs.map { it[index] }.joinToString("") }
                }

                add(Problem(inputs.map { it.trim().toLong() }, operation))
            }
        }

        return list.sumOf { it.solve() }
    }

    private enum class Operation(val symbol: String, val identity: Long, val function: (Long, Long) -> Long) {
        ADD("+", 0L, Long::plus),
        MULTIPLY("*", 1L, Long::times);

        fun fold(inputs: List<Long>): Long {
            return inputs.fold(identity, ::invoke)
        }

        operator fun invoke(a: Long, b: Long): Long = function(a, b)

        companion object {
            fun fromSymbol(symbol: String): Operation {
                return entries.first { it.symbol == symbol }
            }
        }
    }

    private data class Problem(val inputs: List<Long>, val operation: Operation) {
        fun solve() = operation.fold(inputs)
    }
}

fun main() {
    Day06().execute()
}
