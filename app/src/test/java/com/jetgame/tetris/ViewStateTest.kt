package com.jetgame.tetris

import com.jetgame.tetris.logic.GameStatus
import com.jetgame.tetris.logic.GameViewModel.ViewState
import com.jetgame.tetris.logic.Spirit
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * @brief Unit tests of the ViewState data class: defaults, level calculation and status flags.
 */
class ViewStateTest {

    private fun levelOf(lines: Int) = ViewState(line = lines).level

    /**
     * @brief A new game starts with 0 points, 0 lines and level 1 on an empty board.
     * @testcase F-003
     * @technique EP
     */
    @Test
    fun F003_defaultStateIsAFreshGame() {
        val state = ViewState()
        assertEquals(0, state.score)
        assertEquals(0, state.line)
        assertEquals(1, state.level)
        assertEquals(GameStatus.Onboard, state.gameStatus)
        assertEquals(12 to 24, state.matrix)
        assertFalse(state.isMute)
        assertTrue(state.bricks.isEmpty())
        assertEquals(Spirit.Empty, state.spirit)
    }

    /**
     * @brief Level 1 up to 19 lines, level 2 from 20 lines.
     * @testcase F-036
     * @technique BVA
     */
    @Test
    fun F036_levelChangesFromOneToTwoAtTwentyLines() {
        assertEquals(1, levelOf(0))
        assertEquals(1, levelOf(19))
        assertEquals(2, levelOf(20))
    }

    /**
     * @brief Level 2 up to 39 lines, level 3 from 40 lines.
     * @testcase F-037
     * @technique BVA
     */
    @Test
    fun F037_levelChangesFromTwoToThreeAtFortyLines() {
        assertEquals(2, levelOf(39))
        assertEquals(3, levelOf(40))
    }

    /**
     * @brief Level 9 at 179 lines, level 10 at 180 lines.
     * @testcase F-038
     * @technique BVA
     */
    @Test
    fun F038_levelChangesFromNineToTenAtOneHundredEightyLines() {
        assertEquals(9, levelOf(179))
        assertEquals(10, levelOf(180))
    }

    /**
     * @brief The level never exceeds 10 (the display has a single LED digit).
     * @testcase F-038, F-040
     * @technique BVA
     */
    @Test
    fun F040_levelIsCappedAtTen() {
        assertEquals(10, levelOf(181))
        assertEquals(10, levelOf(200))
        assertEquals(10, levelOf(1000))
    }

    /**
     * @brief Only Running counts as running and only Paused counts as paused.
     * @testcase F-004, F-005
     * @technique ST
     */
    @Test
    fun F004_statusFlagsMatchGameStatus() {
        for (status in GameStatus.values()) {
            val state = ViewState(gameStatus = status)
            assertEquals(status.name, status == GameStatus.Running, state.isRuning)
            assertEquals(status.name, status == GameStatus.Paused, state.isPaused)
        }
    }

    /**
     * @brief The next spirit is the first of the reserve, or Empty when there is none.
     * @testcase F-028
     * @technique UC
     */
    @Test
    fun F028_spiritNextIsFirstOfReserve() {
        assertEquals(Spirit.Empty, ViewState().spiritNext)
        val first = spiritOf(TYPE_T, 5, 0)
        val second = spiritOf(TYPE_O, 5, 0)
        assertEquals(first, ViewState(spiritReserve = listOf(first, second)).spiritNext)
    }

    /**
     * @brief The level goes up exactly at every multiple of twenty lines (20, 40, ... 180).
     * @testcase F-061
     * @technique BVA
     */
    @Test
    fun F061_levelChangesAtEveryTwentyLines() {
        for (level in 1..9) {
            assertEquals("before ${20 * level} lines", level, levelOf(20 * level - 1))
            assertEquals("at ${20 * level} lines", level + 1, levelOf(20 * level))
        }
    }
}
