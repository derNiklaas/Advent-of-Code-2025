package de.derniklaas

import utils.AoCDay
import utils.Vec3D
import utils.splitAndMapToInt

class Day08 : AoCDay() {

    val points = input.splitAndMapToInt(",").map { Vec3D(it[0], it[1], it[2]) }
    var distanceBetweenPairs = mutableListOf<PairDistance>()

    init {
        for (i in points.indices) {
            for (j in i + 1 until points.size) {
                val distance = points[i].cartesianDistance(points[j])
                distanceBetweenPairs += PairDistance(setOf(i, j), distance)
            }
        }
        distanceBetweenPairs = distanceBetweenPairs.sortedBy { it.distance }.toMutableList()
    }

    override fun part1(): Any {
        val unionFind = UnionFind(points.size)

        var connections = 0
        for ((locations, _) in distanceBetweenPairs) {
            if (connections >= 1000) break
            unionFind.union(locations.first(), locations.last())
            connections++
        }

        val sizes = unionFind.getCircuitSizes()
        return sizes[0] * sizes[1] * sizes[2]
    }

    override fun part2(): Any {
        val unionFind = UnionFind(points.size)

        for ((locations, _) in distanceBetweenPairs) {
            val a = locations.first()
            val b = locations.last()
            unionFind.union(a, b)
            if (unionFind.getSize(a) == points.size) {
                return points[a].x.toLong() * points[b].x
            }
        }

        return 0
    }

    data class PairDistance(val locations: Set<Int>, val distance: Long)

    class UnionFind(n: Int) {
        private val parent: IntArray = IntArray(n) { it }
        private val rank: IntArray = IntArray(n) { 0 }
        private val size: IntArray = IntArray(n) { 1 }

        fun find(x: Int): Int {
            if (this.parent[x] != x) {
                this.parent[x] = find(this.parent[x])
            }
            return this.parent[x]
        }

        fun union(x: Int, y: Int): Boolean {
            val rootX = find(x)
            val rootY = find(y)

            if (rootX == rootY) return false

            when {
                rank[rootX] < rank[rootY] -> {
                    parent[rootX] = rootY
                    size[rootY] += size[rootX]
                }

                rank[rootX] > rank[rootY] -> {
                    parent[rootY] = rootX
                    size[rootX] += size[rootY]
                }

                else -> {
                    parent[rootY] = rootX
                    size[rootX] += size[rootY]
                    rank[rootX] += 1
                }
            }
            return true
        }

        fun getSize(x: Int): Int {
            return size[find(x)]
        }

        fun getCircuitSizes(): List<Int> {
            val sizes = mutableMapOf<Int, Int>()
            for (i in parent.indices) {
                val root = find(i)
                if (root !in sizes) {
                    sizes[root] = this.size[root]
                }
            }
            val sorted = sizes.values.sortedByDescending { it }
            return sorted
        }
    }
}

fun main() {
    Day08().execute()
}
