package com.jetgame.tetris

import com.jetgame.tetris.logic.Brick
import com.jetgame.tetris.logic.Offset
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @brief Unit tests of Brick: factory methods and shifting.
 */
class BrickTest {

    /**
     * @brief Brick.of(xRange, yRange) creates a full rectangle (used by the screen clearing animation).
     * @testcase F-054
     * @technique EP
     */
    @Test
    fun F054_rangeFactoryCreatesFullRectangle() {
        val bricks = Brick.of(0..2, 0..1)
        assertEquals(6, bricks.size)
        assertEquals(setOf(0 to 0, 1 to 0, 2 to 0, 0 to 1, 1 to 1, 2 to 1), bricks.cells())
    }

    /**
     * @brief offsetBy moves a brick; this is how the rows above a cleared line sink.
     * @testcase F-034
     * @technique EP
     */
    @Test
    fun F034_offsetByMovesBrickByGivenStep() {
        val moved = Brick(Offset(2, 3)).offsetBy(1 to -1)
        assertEquals(3, moved.location.x.toInt())
        assertEquals(2, moved.location.y.toInt())
    }

    /**
     * @brief Brick.of(spirit) converts the four cells of a spirit into locked bricks.
     * @testcase F-055
     * @technique EP
     */
    @Test
    fun F055_spiritFactoryKeepsAllFourCells() {
        val spirit = spiritOf(TYPE_L, 4, 8)
        assertEquals(4, Brick.of(spirit).size)
        assertEquals(spirit.cells(), Brick.of(spirit).cells())
    }
}
