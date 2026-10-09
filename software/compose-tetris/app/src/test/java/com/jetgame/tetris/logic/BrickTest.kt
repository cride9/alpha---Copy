package com.jetgame.tetris.logic

import org.junit.Assert.*
import org.junit.Test

class BrickTest {
    @Test fun rectangleContainsEveryCoordinateOnce() {
        val bricks = Brick.of(2..4, 7..8)
        assertEquals(6, bricks.size)
        assertEquals(setOf(Offset(2, 7), Offset(2, 8), Offset(3, 7), Offset(3, 8), Offset(4, 7), Offset(4, 8)), bricks.map { it.location }.toSet())
    }

    @Test fun emptyXRangeProducesNoBricks() = assertTrue(Brick.of(IntRange.EMPTY, 0..2).isEmpty())
    @Test fun emptyYRangeProducesNoBricks() = assertTrue(Brick.of(0..2, IntRange.EMPTY).isEmpty())

    @Test fun shiftingABrickPreservesTheOriginal() {
        val original = Brick(Offset(3, 7))
        assertEquals(Brick(Offset(2, 9)), original.offsetBy(-1 to 2))
        assertEquals(Offset(3, 7), original.location)
    }

    @Test fun conversionFromCoordinatesPreservesTheirOrder() {
        val cells = listOf(Offset(3, 1), Offset(2, 2), Offset(1, 3))
        assertEquals(cells, Brick.of(cells).map { it.location })
    }
}
