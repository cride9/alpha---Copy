package com.jetgame.tetris.logic

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ShapeContractTest(private val name: String, private val index: Int) {
    @Test fun shapeHasFourDifferentCells() {
        assertEquals(4, SpiritType[index].size)
        assertEquals(4, SpiritType[index].toSet().size)
    }

    @Test fun fourRotationsRestoreThePieceExactly() {
        val original = Spirit(SpiritType[index], Offset(5, 5))
        val rotated = (1..4).fold(original) { piece, _ -> piece.rotate() }
        assertEquals(original, rotated)
    }

    @Test fun everyOrientationFitsOnAnEmptyBoard() {
        var piece = Spirit(SpiritType[index], Offset(5, 5))
        repeat(4) {
            assertTrue(piece.isValidInMatrix(emptyList(), 12 to 24))
            assertEquals(4, piece.location.toSet().size)
            piece = piece.rotate()
        }
    }

    @Test fun conversionToBricksPreservesAllAbsoluteCells() {
        val piece = Spirit(SpiritType[index], Offset(5, 5))
        assertEquals(piece.location.toSet(), Brick.of(piece).map { it.location }.toSet())
    }

    companion object {
        @JvmStatic @Parameterized.Parameters(name = "{0}")
        fun shapes() = listOf("Z", "S", "I", "T", "O", "L", "J")
            .mapIndexed { index, name -> arrayOf<Any>(name, index) }
    }
}
