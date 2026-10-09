package com.jetgame.tetris.logic

import org.junit.Assert.*
import org.junit.Test

/** Tests the original private algorithm without changing the production code. */
class LineClearingIntegrationTest {
    @Suppress("UNCHECKED_CAST")
    private fun clear(bricks: List<Brick>, spirit: Spirit = Spirit.Empty): Pair<Triple<List<Brick>, List<Brick>, List<Brick>>, Int> {
        val method = GameViewModel::class.java.getDeclaredMethod("updateBricks", List::class.java, Spirit::class.java, Pair::class.java)
        method.isAccessible = true
        return method.invoke(GameViewModel(), bricks, spirit, 12 to 24) as Pair<Triple<List<Brick>, List<Brick>, List<Brick>>, Int>
    }

    @Test fun emptyBoardClearsNoRows() {
        val (stages, lines) = clear(emptyList())
        assertEquals(0, lines)
        assertTrue(stages.third.isEmpty())
    }

    @Test fun incompleteRowIsPreserved() {
        val bricks = Brick.of(0..10, 23..23)
        val (stages, lines) = clear(bricks)
        assertEquals(0, lines)
        assertEquals(bricks, stages.third)
    }

    @Test fun aFallingPieceCompletesOneRow() {
        val piece = Spirit(listOf(Offset(0, 0)), Offset(11, 23))
        val (stages, lines) = clear(Brick.of(0..10, 23..23), piece)
        assertEquals(1, lines)
        assertEquals(12, stages.first.size)
        assertTrue(stages.third.isEmpty())
    }

    @Test fun twoCompleteRowsAreRemoved() = assertRowsRemoved(2)
    @Test fun threeCompleteRowsAreRemoved() = assertRowsRemoved(3)
    @Test fun fourCompleteRowsAreRemoved() = assertRowsRemoved(4)

    private fun assertRowsRemoved(count: Int) {
        val (stages, lines) = clear(Brick.of(0..11, (24 - count)..23))
        assertEquals(count, lines)
        assertTrue(stages.third.isEmpty())
    }

    @Test fun cellsAboveTheClearedRowMoveDownAndCellsBelowStay() {
        val remaining = listOf(Brick(Offset(2, 9)), Brick(Offset(3, 12)))
        val (stages, lines) = clear(Brick.of(0..11, 10..10) + remaining)
        assertEquals(1, lines)
        assertEquals(remaining.toSet(), stages.second.toSet())
        assertEquals(setOf(Brick(Offset(2, 10)), Brick(Offset(3, 12))), stages.third.toSet())
    }

    @Test fun separatedFullRowsApplyTheCorrectNumberOfShifts() {
        val bricks = Brick.of(0..11, 10..10) + Brick.of(0..11, 12..12) +
            listOf(Brick(Offset(2, 9)), Brick(Offset(3, 11)), Brick(Offset(4, 13)))
        val (stages, lines) = clear(bricks)
        assertEquals(2, lines)
        assertEquals(setOf(Brick(Offset(2, 11)), Brick(Offset(3, 12)), Brick(Offset(4, 13))), stages.third.toSet())
    }

    @Test fun aCompleteTopRowCanBeRemoved() {
        val (stages, lines) = clear(Brick.of(0..11, 0..0))
        assertEquals(1, lines)
        assertTrue(stages.third.isEmpty())
    }
}
