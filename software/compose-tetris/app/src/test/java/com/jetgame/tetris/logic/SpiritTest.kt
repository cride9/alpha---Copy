package com.jetgame.tetris.logic

import org.junit.Assert.*
import org.junit.Test

class SpiritTest {
    private val matrix = 12 to 24

    @Test fun locationIncludesTheOriginOffset() {
        val spirit = Spirit(listOf(Offset(0, 0), Offset(1, 0)), Offset(4, 8))
        assertEquals(listOf(Offset(4, 8), Offset(5, 8)), spirit.location)
    }

    @Test fun movementDoesNotMutateTheOriginalPiece() {
        val original = Spirit(SpiritType[3], Offset(5, 5))
        val moved = original.moveBy(-1 to 2)
        assertEquals(Offset(5, 5), original.offset)
        assertEquals(Offset(4, 7), moved.offset)
        assertEquals(original.shape, moved.shape)
    }

    @Test fun rotationKeepsTheOriginAndDoesNotMutateTheOriginal() {
        val original = Spirit(listOf(Offset(0, 1), Offset(1, 0)), Offset(5, 5))
        // A quarter turn may produce -0.0; compare geometry, not packed float bits.
        val rotated = original.rotate().shape
        assertEquals(2, rotated.size)
        assertEquals(1f, rotated[0].x, 0f)
        assertEquals(0f, rotated[0].y, 0f)
        assertEquals(0f, rotated[1].x, 0f)
        assertEquals(-1f, rotated[1].y, 0f)
        assertEquals(Offset(5, 5), original.rotate().offset)
        assertEquals(listOf(Offset(0, 1), Offset(1, 0)), original.shape)
    }

    @Test fun leftWallAdjustmentPlacesTheLeftmostCellAtZero() {
        val adjusted = Spirit(SpiritType[2], Offset(-1, 5)).rotate().adjustOffset(matrix)
        assertEquals(0f, adjusted.location.minOf { it.x }, 0f)
        assertTrue(adjusted.isValidInMatrix(emptyList(), matrix))
    }

    @Test fun rightWallAdjustmentPlacesTheRightmostCellAtEleven() {
        val adjusted = Spirit(SpiritType[2], Offset(11, 5)).rotate().adjustOffset(matrix)
        assertEquals(11f, adjusted.location.maxOf { it.x }, 0f)
        assertTrue(adjusted.isValidInMatrix(emptyList(), matrix))
    }

    @Test fun floorAdjustmentPlacesTheLowestCellAtTwentyThree() {
        val adjusted = Spirit(SpiritType[2], Offset(5, 23)).adjustOffset(matrix)
        assertEquals(23f, adjusted.location.maxOf { it.y }, 0f)
    }

    @Test fun spawnAdjustmentCanPreserveCellsAboveTheBoard() {
        val adjusted = Spirit(SpiritType[2], Offset(5, -1)).adjustOffset(matrix, false)
        assertEquals(-2f, adjusted.location.minOf { it.y }, 0f)
        assertTrue(adjusted.isValidInMatrix(emptyList(), matrix))
    }

    @Test fun collisionWithOneOccupiedCellRejectsTheEntirePiece() {
        val spirit = Spirit(SpiritType[3], Offset(5, 5))
        assertFalse(spirit.isValidInMatrix(listOf(Brick(Offset(5, 5))), matrix))
    }

    @Test fun adjacentOccupiedCellsAllowMovement() {
        val spirit = Spirit(SpiritType[2], Offset(5, 5))
        assertTrue(spirit.isValidInMatrix(listOf(Brick(Offset(6, 5))), matrix))
    }

    @Test fun emptyPieceDoesNotOccupyAnyCells() {
        assertTrue(Spirit.Empty.location.isEmpty())
        assertTrue(Spirit.Empty.isValidInMatrix(Brick.of(0..11, 0..23), matrix))
    }

    @Test fun reserveContainsEveryShapeExactlyOnceAndFitsHorizontally() {
        repeat(20) {
            val reserve = generateSpiritReverse(matrix)
            assertEquals(7, reserve.size)
            assertEquals(SpiritType.toSet(), reserve.map { it.shape }.toSet())
            assertTrue(reserve.all { it.location.all { cell -> cell.x in 0f..11f } })
        }
    }
}
