package de.derniklaas

import kotlin.collections.set
import kotlin.collections.toMutableSet
import utils.AoCDay

class Day11 : AoCDay() {

    val adjacencyList = mutableMapOf<String, MutableSet<String>>()

    override fun part1(): Any {
        input.forEach { line ->
            val start = line.substringBefore(":")
            val end = line.substringAfter(": ").split(" ")
            val oldValue = adjacencyList[start] ?: mutableListOf()
            oldValue.addAll(end)
            adjacencyList[start] = oldValue.toMutableSet()
        }

        return countPaths("you", mutableSetOf())
    }

    override fun part2(): Any {
        return countPaths("svr", mutableSetOf(), false, false)
    }

    val cache = mutableMapOf<String, Int>()

    fun countPaths(node: String, visiting: MutableSet<String>): Int {
        if (node == "out") return 1
        if (node in visiting) return 0
        if (node in cache) return cache[node]!!

        visiting.add(node)

        val neighbours = adjacencyList[node] ?: mutableSetOf()
        var paths = 0
        for (neighbour in neighbours) {
            paths += countPaths(neighbour, visiting)
        }

        visiting.remove(node)
        cache[node] = paths
        return paths
    }

    val cacheB = mutableMapOf<String, Map<Pair<Boolean, Boolean>, Long>>()

    fun countPaths(node: String, visiting: MutableSet<String>, visitedFFT: Boolean, visitedDAC: Boolean): Long {
        if (node == "out") return if (visitedFFT && visitedDAC) 1 else 0
        if (node in visiting) return 0

        val state = visitedFFT to visitedDAC
        val stateCache = cacheB[node] ?: emptyMap()

        if (state in stateCache) return stateCache[state]!!

        visiting.add(node)

        val neighbours = adjacencyList[node] ?: mutableSetOf()
        var paths = 0L
        for (neighbour in neighbours) {
            paths += countPaths(neighbour, visiting, visitedFFT || node == "fft", visitedDAC || node == "dac")
        }

        visiting.remove(node)
        cacheB[node] = stateCache + (state to paths)
        return paths
    }
}

fun main() {
    Day11().execute()
}
