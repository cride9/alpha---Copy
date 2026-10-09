package com.jetgame.tetris

import com.jetgame.tetris.logic.Brick
import com.jetgame.tetris.logic.GameViewModel
import com.jetgame.tetris.logic.Offset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * @brief Unit tests of the line clearing algorithm (private GameViewModel.updateBricks).
 *
 * A vertical I piece at (11, 21) covers column 11, rows 20..23. The prepared rows contain
 * columns 0..10, so the piece fills the only missing cell of every prepared row.
 */
class LineClearTest {

    private val vm = GameViewModel()
    private val piece = spiritOf(TYPE_I, 11, 21)

    /**
     * @brief One full row is removed and the rest of the piece sinks by one row.
     * @testcase F-029
     * @technique BVA
     */
    @Test
    fun F029_oneCompleteRowIsCleared() {
        val result = callUpdateBricks(vm, rowBricks(23, 0..10), piece)
        assertEquals(1, result.lines)
        assertEquals(15, result.before.size)
        assertEquals(3, result.clearing.size)
        assertEquals(setOf(11 to 21, 11 to 22, 11 to 23), result.cleared.cells())
    }

    /**
     * @brief Two full rows are removed at once.
     * @testcase F-030
     * @technique BVA
     */
    @Test
    fun F030_twoCompleteRowsAreCleared() {
        val bricks = rowBricks(22, 0..10) + rowBricks(23, 0..10)
        val result = callUpdateBricks(vm, bricks, piece)
        assertEquals(2, result.lines)
        assertEquals(setOf(11 to 22, 11 to 23), result.cleared.cells())
    }

    /**
     * @brief Three full rows are removed at once.
     * @testcase F-031
     * @technique BVA
     */
    @Test
    fun F031_threeCompleteRowsAreCleared() {
        val bricks = rowBricks(21, 0..10) + rowBricks(22, 0..10) + rowBricks(23, 0..10)
        val result = callUpdateBricks(vm, bricks, piece)
        assertEquals(3, result.lines)
        assertEquals(setOf(11 to 23), result.cleared.cells())
    }

    /**
     * @brief Four full rows are removed at once and the board is empty.
     * @testcase F-032
     * @technique BVA
     */
    @Test
    fun F032_fourCompleteRowsAreCleared() {
        val bricks = (20..23).flatMap { rowBricks(it, 0..10) }
        val result = callUpdateBricks(vm, bricks, piece)
        assertEquals(4, result.lines)
        assertEquals(emptySet<Pair<Int, Int>>(), result.cleared.cells())
    }

    /**
     * @brief A row with a missing cell is not cleared.
     * @testcase F-033
     * @technique EP
     */
    @Test
    fun F033_incompleteRowIsNotCleared() {
        val bricks = rowBricks(23, 0..9)
        val result = callUpdateBricks(vm, bricks, piece)
        assertEquals(0, result.lines)
        assertEquals(result.before.cells(), result.cleared.cells())
    }

    /**
     * @brief Bricks above a cleared row sink, bricks below it stay in place.
     * @testcase F-034
     * @technique EP
     */
    @Test
    fun F034_bricksAboveSinkAndBricksBelowStay() {
        val bricks = rowBricks(22, 0..10) +
                Brick.of(listOf(Offset(0, 23), Offset(1, 23), Offset(3, 21), Offset(4, 21)))
        val result = callUpdateBricks(vm, bricks, piece)
        assertEquals(1, result.lines)
        assertEquals(
            setOf(0 to 23, 1 to 23, 11 to 23, 3 to 22, 4 to 22, 11 to 22, 11 to 21),
            result.cleared.cells()
        )
    }

    /**
     * @brief Locking a piece on an empty board only adds its four bricks.
     * @testcase F-035
     * @technique EP
     */
    @Test
    fun F035_lockingWithoutClearOnlyAddsTheBricks() {
        val result = callUpdateBricks(vm, emptyList(), piece)
        assertEquals(0, result.lines)
        assertEquals(4, result.before.size)
        assertEquals(piece.cells(), result.cleared.cells())
    }

    /**
     * @brief Two full rows with an incomplete row between them are cleared, the middle row stays.
     * @testcase F-064
     * @technique EP
     */
    @Test
    fun F064_twoNonAdjacentRowsAreClearedTogether() {
        val bricks = rowBricks(20, 0..10) + rowBricks(21, 1..10) + rowBricks(22, 0..10)
        val result = callUpdateBricks(vm, bricks, piece)
        assertEquals(2, result.lines)
        val expected = (1..11).map { it to 22 }.toSet() + setOf(11 to 23)
        assertEquals(expected, result.cleared.cells())
    }

    /**
     * @brief After a clear every remaining brick is still inside the 12 x 24 board.
     * @testcase F-065
     * @technique BVA
     */
    @Test
    fun F065_clearedBricksStayInsideTheBoard() {
        val bricks = (20..23).flatMap { rowBricks(it, 0..10) }
        val result = callUpdateBricks(vm, bricks + rowBricks(19, 0..5), piece)
        assertTrue(result.cleared.cells().all { it.first in 0..11 && it.second in 0..23 })
    }

    /**
     * @brief Locking a piece without a clear keeps all existing bricks unchanged.
     * @testcase F-069
     * @technique EP
     */
    @Test
    fun F069_lockedPieceWithoutClearKeepsExistingBricks() {
        val existing = rowBricks(23, 0..3)
        val locked = spiritOf(TYPE_I, 8, 21)
        val result = callUpdateBricks(vm, existing, locked)
        assertEquals(0, result.lines)
        assertEquals(existing.cells() + locked.cells(), result.cleared.cells())
    }
}
