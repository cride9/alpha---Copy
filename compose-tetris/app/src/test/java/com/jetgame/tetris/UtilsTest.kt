package com.jetgame.tetris

import com.jetgame.tetris.logic.Direction
import com.jetgame.tetris.logic.ScoreEverySpirit
import com.jetgame.tetris.logic.calculateScore
import com.jetgame.tetris.logic.toOffset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * @brief Unit tests of the helper functions in Utils.kt (scoring and directions).
 */
class UtilsTest {

    /**
     * @brief Clearing 1, 2, 3 and 4 lines gives 100, 300, 700 and 1500 points.
     * @testcase F-029, F-030, F-031, F-032
     * @technique BVA
     */
    @Test
    fun F029_F032_scoreForOneToFourLinesMatchesReadmeTable() {
        assertEquals(100, calculateScore(1))
        assertEquals(300, calculateScore(2))
        assertEquals(700, calculateScore(3))
        assertEquals(1500, calculateScore(4))
    }

    /**
     * @brief No line clear (or an impossible line count) gives no line points.
     * @testcase F-035
     * @technique BVA
     */
    @Test
    fun F035_scoreIsZeroForNoLinesOrInvalidCount() {
        assertEquals(0, calculateScore(0))
        assertEquals(0, calculateScore(5))
        assertEquals(0, calculateScore(-1))
    }

    /**
     * @brief Every locked piece is worth 12 extra points.
     * @testcase F-035
     * @technique EP
     */
    @Test
    fun F035_lockedPieceBonusIsTwelvePoints() {
        assertEquals(12, ScoreEverySpirit)
    }

    /**
     * @brief The four directions map to the expected (dx, dy) steps.
     * @testcase F-009, F-010, F-011
     * @technique EP
     */
    @Test
    fun F009_F011_directionsMapToExpectedSteps() {
        assertEquals(-1 to 0, Direction.Left.toOffset())
        assertEquals(1 to 0, Direction.Right.toOffset())
        assertEquals(0 to 1, Direction.Down.toOffset())
        assertEquals(0 to -1, Direction.Up.toOffset())
    }

    /**
     * @brief Clearing more lines at once always gives more points than clearing fewer.
     * @testcase F-060
     * @technique BVA
     */
    @Test
    fun F060_scoreGrowsWithTheNumberOfClearedLines() {
        assertTrue(calculateScore(1) < calculateScore(2))
        assertTrue(calculateScore(2) < calculateScore(3))
        assertTrue(calculateScore(3) < calculateScore(4))
    }

    /**
     * @brief Four lines at once are worth more than four single lines (Tetris bonus).
     * @testcase F-060
     * @technique BVA
     */
    @Test
    fun F060_fourLineClearIsWorthMoreThanFourSingleLines() {
        assertTrue(calculateScore(4) > 4 * calculateScore(1))
        assertTrue(calculateScore(4) > 2 * calculateScore(2))
    }
}
