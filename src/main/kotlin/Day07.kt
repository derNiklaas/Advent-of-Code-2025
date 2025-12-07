package de.derniklaas

import utils.AoCDay
import utils.Vec2D
import utils.toPointMap

class Day07 : AoCDay() {
    val map = input.toPointMap()
    val start = map.entries.first { it.value == 'S' }.key

    override fun part1(): Any {
        val queue = mutableSetOf(start)
        var splits = 0

        while (queue.isNotEmpty()) {
            val pos = queue.first()
            queue.remove(pos)
            val downWards = pos + Vec2D.DOWN
            if (map[pos] == '^') {
                splits++
                queue += downWards + Vec2D.LEFT
                queue += downWards + Vec2D.RIGHT
            } else if (downWards in map) {
                queue += downWards
            }
        }

        return splits
    }

    override fun part2(): Any {
        return countTimelines(start)
    }

    val cache = mutableMapOf<Vec2D, Long>()

    fun countTimelines(from: Vec2D): Long {
        val downWards = from + Vec2D.DOWN
        if (from in cache) return cache[from]!!
        val output = when {
            map[from] == '^' -> {
                countTimelines(downWards + Vec2D.LEFT) + countTimelines(downWards + Vec2D.RIGHT)
            }

            downWards in map -> {
                countTimelines(downWards)
            }

            else -> {
                1L
            }
        }
        cache[from] = output
        return output
    }
}

fun main() {
    Day07().execute()
}
