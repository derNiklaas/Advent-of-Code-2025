package de.derniklaas.utils

import utils.Vec2D

data class Line(val from: Vec2D, val to: Vec2D) {
    val isVertical get() = from.x == to.x
    val isHorizontal get() = from.y == to.y
    val isStraight get() = isVertical || isHorizontal

    /**
     * Reoders the line so that [from] is always the smaller point and [to] the larger point.
     * Returns a new Line instance
     */
    fun reorder(): Line {
        return Line(
            Vec2D(minOf(from.x, to.x), minOf(from.y, to.y)),
            Vec2D(maxOf(from.x, to.x), maxOf(from.y, to.y))
        )
    }
}