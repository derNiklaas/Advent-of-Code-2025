package de.derniklaas.utils

import kotlin.math.abs
import utils.Vec2D

data class Rectangle(val a: Vec2D, val b: Vec2D) {
    val width get() = abs(a.x - b.x) + 1
    val height get() = abs(a.y - b.y) + 1
    val minX get() = minOf(a.x, b.x)
    val maxX get() = maxOf(a.x, b.x)
    val minY get() = minOf(a.y, b.y)
    val maxY get() = maxOf(a.y, b.y)
    val xRange get() = minX..maxX
    val yRange get() = minY..maxY

    val area get() = width.toLong() * height.toLong()

    /**
     * Reoders the rectangle so that [a] is always the top-left point and [b] the bottom-right point.
     * Returns a new Rectangle instance
     */
    fun reorder(): Rectangle {
        return Rectangle(
            Vec2D(minOf(a.x, b.x), minOf(a.y, b.y)),
            Vec2D(maxOf(a.x, b.x), maxOf(a.y, b.y))
        )
    }

    /**
     * Returns a new Rectangle contracted by [amount] on all sides.
     */
    fun contract(amount: Int) = Rectangle(
        Vec2D(a.x + amount, a.y + amount),
        Vec2D(b.x - amount, b.y - amount)
    )

    /**
     * Checks if this rectangle intersects with the given [line]. Only horizontal and vertical lines are supported.
     */
    fun intersects(line: Line) = when {
        line.isVertical -> intersectsStraights(line, Vec2D::x, Vec2D::y, Rectangle::xRange)
        line.isHorizontal -> intersectsStraights(line, Vec2D::y, Vec2D::x, Rectangle::yRange)
        else -> error("Only horizontal and vertical lines are supported")
    }

    /**
     * Checks if this rectangle intersects with the given straight [line].
     */
    fun intersectsStraights(
        line: Line,
        coord: (Vec2D) -> Int,
        opposite: (Vec2D) -> Int,
        range: (Rectangle) -> IntRange
    ) = coord(line.from) in range(contract(1)) && maxOf(opposite(a), opposite(line.from)) < minOf(
        opposite(b),
        opposite(line.to)
    )
}