package com.jetgame.tetris.logic

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ViewStateLevelTest(private val lines: Int, private val expectedLevel: Int) {
    @Test fun levelChangesAtTwentyLineBoundariesAndStopsAtTen() {
        assertEquals(expectedLevel, GameViewModel.ViewState(line = lines).level)
    }
    companion object {
        @JvmStatic @Parameterized.Parameters(name = "{0} lines -> level {1}")
        fun cases() = listOf(0 to 1, 19 to 1, 20 to 2, 21 to 2, 39 to 2, 40 to 3, 179 to 9, 180 to 10, 199 to 10, 200 to 10, 999 to 10)
            .map { arrayOf(it.first, it.second) }
    }
}

class ViewStateTest {
    @Test fun initialStateHasEmptyBoardAndZeroCounters() {
        val state = GameViewModel.ViewState()
        assertEquals(0, state.score)
        assertEquals(0, state.line)
        assertEquals(1, state.level)
        assertEquals(12 to 24, state.matrix)
        assertEquals(GameStatus.Onboard, state.gameStatus)
        assertTrue(state.bricks.isEmpty())
        assertFalse(state.isPaused)
        assertFalse(state.isRuning)
    }

    @Test fun nextPieceIsTheFirstReserveEntry() {
        val first = Spirit(SpiritType[0], Offset(5, -1))
        val second = Spirit(SpiritType[1], Offset(5, -1))
        assertEquals(first, GameViewModel.ViewState(spiritReserve = listOf(first, second)).spiritNext)
    }

    @Test fun emptyReserveExposesAnEmptyNextPiece() = assertEquals(Spirit.Empty, GameViewModel.ViewState().spiritNext)

    @Test fun statusFlagsDistinguishEveryGameState() {
        GameStatus.values().forEach { status ->
            val state = GameViewModel.ViewState(gameStatus = status)
            assertEquals(status == GameStatus.Running, state.isRuning)
            assertEquals(status == GameStatus.Paused, state.isPaused)
        }
    }
}
