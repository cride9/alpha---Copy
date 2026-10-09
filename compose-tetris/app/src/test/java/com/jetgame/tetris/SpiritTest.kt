package com.jetgame.tetris

import com.jetgame.tetris.logic.Brick
import com.jetgame.tetris.logic.Direction
import com.jetgame.tetris.logic.Offset
import com.jetgame.tetris.logic.Spirit
import com.jetgame.tetris.logic.SpiritType
import com.jetgame.tetris.logic.generateSpiritReverse
import com.jetgame.tetris.logic.isValidInMatrix
import com.jetgame.tetris.logic.toOffset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * @brief Unit tests of Spirit: movement, rotation, wall/brick collision and spawning.
 *
 * Test names start with the ID of the functional test case they support (see TestSupport.kt).
 */
class SpiritTest {

    private val empty = emptyList<Brick>()

    /**
     * @brief The location of a spirit is its shape shifted by its offset.
     * @testcase F-051
     * @technique EP
     */
    @Test
    fun F051_locationIsShapePlusOffset() {
        assertEquals(setOf(5 to 9, 5 to 10, 5 to 11, 5 to 12), spiritOf(TYPE_I, 5, 10).cells())
    }

    /**
     * @brief Moving left shifts the spirit by one column to the left.
     * @testcase F-009
     * @technique EP
     */
    @Test
    fun F009_moveLeftShiftsSpiritOneColumn() {
        val moved = spiritOf(TYPE_I, 5, 10).moveBy(Direction.Left.toOffset())
        assertEquals(setOf(4 to 9, 4 to 10, 4 to 11, 4 to 12), moved.cells())
    }

    /**
     * @brief Moving right shifts the spirit by one column to the right.
     * @testcase F-010
     * @technique EP
     */
    @Test
    fun F010_moveRightShiftsSpiritOneColumn() {
        val moved = spiritOf(TYPE_I, 5, 10).moveBy(Direction.Right.toOffset())
        assertEquals(setOf(6 to 9, 6 to 10, 6 to 11, 6 to 12), moved.cells())
    }

    /**
     * @brief Moving down shifts the spirit by one row down.
     * @testcase F-011
     * @technique EP
     */
    @Test
    fun F011_moveDownShiftsSpiritOneRow() {
        val moved = spiritOf(TYPE_I, 5, 10).moveBy(Direction.Down.toOffset())
        assertEquals(setOf(5 to 10, 5 to 11, 5 to 12, 5 to 13), moved.cells())
    }

    /**
     * @brief All seven shapes have four distinct cells, also after rotation.
     * @testcase F-023
     * @technique EP
     */
    @Test
    fun F023_allSevenShapesKeepFourCellsAfterRotation() {
        assertEquals(7, SpiritType.size)
        for (type in SpiritType.indices) {
            val spirit = spiritOf(type, 5, 10)
            assertEquals("shape $type", 4, spirit.cells().size)
            assertEquals("rotated shape $type", 4, spirit.rotate().cells().size)
        }
    }

    /**
     * @brief Rotating a T shape in free space turns it by 90 degrees.
     * @testcase F-017
     * @technique EP
     */
    @Test
    fun F017_rotateTShapeInFreeSpaceTurnsNinetyDegrees() {
        val rotated = spiritOf(TYPE_T, 5, 10).rotate().adjustOffset(MATRIX)
        assertEquals(setOf(4 to 10, 5 to 10, 6 to 10, 5 to 9), rotated.cells())
    }

    /**
     * @brief Four rotations restore the original orientation and position of every shape.
     * @testcase F-018
     * @technique EP
     */
    @Test
    fun F018_fourRotationsRestoreOriginalCells() {
        for (type in SpiritType.indices) {
            var spirit = spiritOf(type, 5, 10)
            repeat(4) { spirit = spirit.rotate() }
            assertEquals("shape $type", spiritOf(type, 5, 10).cells(), spirit.cells())
        }
    }

    /**
     * @brief Rotating the vertical I at the left wall is pushed back into the board.
     * @testcase F-019
     * @technique BVA
     */
    @Test
    fun F019_rotateAtLeftWallStaysInsideBoard() {
        val rotated = spiritOf(TYPE_I, 0, 10).rotate().adjustOffset(MATRIX)
        assertEquals(setOf(0 to 10, 1 to 10, 2 to 10, 3 to 10), rotated.cells())
        assertTrue(rotated.isValidInMatrix(empty, MATRIX))
    }

    /**
     * @brief Rotating the vertical I at the right wall is pushed back into the board.
     * @testcase F-020
     * @technique BVA
     */
    @Test
    fun F020_rotateAtRightWallStaysInsideBoard() {
        val rotated = spiritOf(TYPE_I, 11, 10).rotate().adjustOffset(MATRIX)
        assertEquals(setOf(8 to 10, 9 to 10, 10 to 10, 11 to 10), rotated.cells())
        assertTrue(rotated.isValidInMatrix(empty, MATRIX))
    }

    /**
     * @brief Rotating on the bottom row does not push the spirit below the board.
     * @testcase F-021
     * @technique BVA
     */
    @Test
    fun F021_rotateAtBottomStaysInsideBoard() {
        val horizontal = spiritOf(TYPE_I, 5, 23).rotate()
        assertEquals(setOf(4 to 23, 5 to 23, 6 to 23, 7 to 23), horizontal.cells())
        val vertical = horizontal.rotate().adjustOffset(MATRIX)
        assertEquals(setOf(5 to 20, 5 to 21, 5 to 22, 5 to 23), vertical.cells())
        assertTrue(vertical.isValidInMatrix(empty, MATRIX))
    }

    /**
     * @brief A rotation that would overlap a locked brick is not valid.
     * @testcase F-022
     * @technique EP
     */
    @Test
    fun F022_rotationOverlappingLockedBrickIsInvalid() {
        val blocks = Brick.of(listOf(Offset(4, 10)))
        val spirit = spiritOf(TYPE_T, 5, 10)
        assertTrue(spirit.isValidInMatrix(blocks, MATRIX))
        assertFalse(spirit.rotate().adjustOffset(MATRIX).isValidInMatrix(blocks, MATRIX))
    }

    /**
     * @brief The spawn adjustment may keep rows above the board (adjustY = false).
     * @testcase F-052
     * @technique BVA
     */
    @Test
    fun F052_adjustOffsetWithoutYKeepsRowsAboveTheBoard() {
        val spawned = Spirit(SpiritType[TYPE_I], Offset(3, -1))
        assertEquals(setOf(3 to -2, 3 to -1, 3 to 0, 3 to 1), spawned.adjustOffset(MATRIX, false).cells())
        assertEquals(setOf(3 to 0, 3 to 1, 3 to 2, 3 to 3), spawned.adjustOffset(MATRIX, true).cells())
    }

    /**
     * @brief A spirit cannot leave the board through the left wall.
     * @testcase F-012
     * @technique BVA
     */
    @Test
    fun F012_leftWallBlocksMovement() {
        val moved = spiritOf(TYPE_I, 0, 10).moveBy(Direction.Left.toOffset())
        assertFalse(moved.isValidInMatrix(empty, MATRIX))
    }

    /**
     * @brief A spirit cannot leave the board through the right wall.
     * @testcase F-013
     * @technique BVA
     */
    @Test
    fun F013_rightWallBlocksMovement() {
        val moved = spiritOf(TYPE_I, 11, 10).moveBy(Direction.Right.toOffset())
        assertFalse(moved.isValidInMatrix(empty, MATRIX))
    }

    /**
     * @brief A spirit cannot move into a locked brick.
     * @testcase F-014
     * @technique EP
     */
    @Test
    fun F014_lockedBrickBlocksSidewaysMovement() {
        val blocks = Brick.of(listOf(Offset(6, 10)))
        val moved = spiritOf(TYPE_I, 5, 10).moveBy(Direction.Right.toOffset())
        assertFalse(moved.isValidInMatrix(blocks, MATRIX))
    }

    /**
     * @brief The bottom row is valid, one row below it is not.
     * @testcase F-015
     * @technique BVA
     */
    @Test
    fun F015_bottomOfBoardBlocksMovement() {
        val onBottom = spiritOf(TYPE_I, 5, 21)
        assertTrue(onBottom.isValidInMatrix(empty, MATRIX))
        assertFalse(onBottom.moveBy(Direction.Down.toOffset()).isValidInMatrix(empty, MATRIX))
    }

    /**
     * @brief A spirit that is still above the top edge is valid (new pieces spawn there).
     * @testcase F-052
     * @technique BVA
     */
    @Test
    fun F052_spiritAboveTopEdgeIsValid() {
        assertTrue(spiritOf(TYPE_I, 5, -3).isValidInMatrix(empty, MATRIX))
    }

    /**
     * @brief A generated bag contains each of the seven shapes exactly once.
     * @testcase F-028
     * @technique UC
     */
    @Test
    fun F028_generatedBagContainsEachOfTheSevenShapes() {
        val bag = generateSpiritReverse(MATRIX)
        assertEquals(7, bag.size)
        assertEquals(SpiritType.toSet(), bag.map { it.shape }.toSet())
    }

    /**
     * @brief Generated pieces always spawn inside the board columns (random x offset).
     * @testcase F-053
     * @technique BVA
     */
    @Test
    fun F053_generatedSpiritsSpawnInsideBoardColumns() {
        repeat(200) {
            generateSpiritReverse(MATRIX).forEach { spirit ->
                assertTrue(spirit.location.all { it.x >= 0f && it.x <= 11f })
            }
        }
    }

    /**
     * @brief The O shape stays a 2x2 square after rotation (it only shifts by one column).
     * @testcase F-056
     * @technique EP
     */
    @Test
    fun F056_oShapeRotationKeepsSquare() {
        val rotated = spiritOf(TYPE_O, 5, 10).rotate().adjustOffset(MATRIX)
        assertEquals(setOf(4 to 9, 4 to 10, 5 to 9, 5 to 10), rotated.cells())
    }

    /**
     * @brief The I shape alternates between vertical and horizontal on every rotation.
     * @testcase F-057
     * @technique EP
     */
    @Test
    fun F057_iShapeRotationAlternatesBetweenVerticalAndHorizontal() {
        val vertical = spiritOf(TYPE_I, 5, 10)
        val horizontal = vertical.rotate().adjustOffset(MATRIX)
        assertEquals(setOf(4 to 10, 5 to 10, 6 to 10, 7 to 10), horizontal.cells())
        val verticalAgain = horizontal.rotate().adjustOffset(MATRIX)
        assertEquals(setOf(5 to 8, 5 to 9, 5 to 10, 5 to 11), verticalAgain.cells())
    }

    /**
     * @brief Every shape placed at the left wall stays valid and inside the board after rotation.
     * @testcase F-058
     * @technique BVA
     */
    @Test
    fun F058_everyShapeRotatedAtLeftWallStaysInsideBoard() {
        for (type in SpiritType.indices) {
            val minX = SpiritType[type].minOf { it.x }.toInt()
            val rotated = spiritOf(type, -minX, 10).rotate().adjustOffset(MATRIX)
            assertTrue("shape $type", rotated.isValidInMatrix(empty, MATRIX))
            assertTrue("shape $type", rotated.cells().all { it.first in 0..11 })
        }
    }

    /**
     * @brief Every shape placed at the right wall stays valid and inside the board after rotation.
     * @testcase F-059
     * @technique BVA
     */
    @Test
    fun F059_everyShapeRotatedAtRightWallStaysInsideBoard() {
        for (type in SpiritType.indices) {
            val maxX = SpiritType[type].maxOf { it.x }.toInt()
            val rotated = spiritOf(type, 11 - maxX, 10).rotate().adjustOffset(MATRIX)
            assertTrue("shape $type", rotated.isValidInMatrix(empty, MATRIX))
            assertTrue("shape $type", rotated.cells().all { it.first in 0..11 })
        }
    }
}
