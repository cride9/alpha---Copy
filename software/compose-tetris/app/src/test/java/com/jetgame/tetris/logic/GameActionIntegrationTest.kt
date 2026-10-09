package com.jetgame.tetris.logic

import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GameActionIntegrationTest {
    private lateinit var model: GameViewModel

    @Before fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        model = GameViewModel()
    }

    @After fun tearDown() {
        model.viewModelScope.cancel()
        Dispatchers.resetMain()
    }

    @Suppress("UNCHECKED_CAST")
    private fun given(state: GameViewModel.ViewState) {
        val field = GameViewModel::class.java.getDeclaredField("_viewState")
        field.isAccessible = true
        (field.get(model) as MutableState<GameViewModel.ViewState>).value = state.copy(isMute = true)
    }

    private fun awaitState(predicate: (GameViewModel.ViewState) -> Boolean): GameViewModel.ViewState {
        val deadline = System.nanoTime() + 5_000_000_000L
        while (System.nanoTime() < deadline) {
            val state = model.viewState.value
            if (predicate(state)) return state
            Thread.sleep(5)
        }
        fail("State transition did not finish within 5 seconds: ${model.viewState.value}")
        return model.viewState.value
    }

    private fun running(piece: Spirit = Spirit(SpiritType[3], Offset(5, 5))) =
        GameViewModel.ViewState(spirit = piece, gameStatus = GameStatus.Running, isMute = true)

    @Test fun resetFromWelcomeStartsTheGameAndPreservesMute() {
        given(GameViewModel.ViewState())
        model.dispatch(Action.Reset)
        assertTrue(awaitState { it.isRuning }.isMute)
    }

    @Test fun pausePreservesBoardAndCounters() {
        val state = running().copy(score = 100, line = 3, bricks = listOf(Brick(Offset(0, 23))))
        given(state)
        model.dispatch(Action.Pause)
        assertEquals(state.copy(gameStatus = GameStatus.Paused), awaitState { it.isPaused })
    }

    @Test fun resumePreservesThePausedGame() {
        val state = running().copy(gameStatus = GameStatus.Paused, score = 100)
        given(state)
        model.dispatch(Action.Resume)
        assertEquals(state.copy(gameStatus = GameStatus.Running), awaitState { it.isRuning })
    }

    @Test fun muteTogglesWithoutChangingTheBoard() {
        val state = running()
        given(state)
        model.dispatch(Action.Mute)
        assertEquals(state.copy(isMute = false), awaitState { !it.isMute })
        model.dispatch(Action.Mute)
        assertEquals(state, awaitState { it.isMute })
    }

    @Test fun moveLeftUsesTheMovementAndCollisionComponents() = assertMove(Direction.Left, -1 to 0)
    @Test fun moveRightUsesTheMovementAndCollisionComponents() = assertMove(Direction.Right, 1 to 0)
    @Test fun moveDownUsesTheMovementAndCollisionComponents() = assertMove(Direction.Down, 0 to 1)

    private fun assertMove(direction: Direction, offset: Pair<Int, Int>) {
        val state = running()
        given(state)
        model.dispatch(Action.Move(direction))
        assertEquals(state.spirit.moveBy(offset), awaitState { it.spirit != state.spirit }.spirit)
    }

    @Test fun rotationChangesTheActivePiece() {
        val state = running()
        given(state)
        model.dispatch(Action.Rotate)
        assertEquals(state.spirit.rotate(), awaitState { it.spirit != state.spirit }.spirit)
    }

    @Test fun dropFindsTheLastValidPositionWithoutChangingTheScore() {
        val state = running(Spirit(SpiritType[2], Offset(5, 5)))
        given(state)
        model.dispatch(Action.Drop)
        val dropped = awaitState { it.spirit != state.spirit }
        assertEquals(23f, dropped.spirit.location.maxOf { it.y }, 0f)
        assertTrue(dropped.spirit.isValidInMatrix(dropped.bricks, dropped.matrix))
        assertFalse(dropped.spirit.moveBy(0 to 1).isValidInMatrix(dropped.bricks, dropped.matrix))
        assertEquals(0, dropped.score)
    }

    @Test fun tickMovesAnUnblockedPieceDownOneCell() {
        val state = running()
        given(state)
        model.dispatch(Action.GameTick)
        assertEquals(state.spirit.moveBy(0 to 1), awaitState { it.spirit != state.spirit }.spirit)
    }

    @Test fun lockingAPieceAddsItsCellsAndAwardsThePlacementBonusOnce() {
        val piece = Spirit(SpiritType[2], Offset(5, 21))
        val next = Spirit(SpiritType[0], Offset(5, -1))
        given(running(piece).copy(spiritReserve = listOf(next)))
        model.dispatch(Action.GameTick)
        val locked = awaitState { it.score == 12 }
        assertEquals(Brick.of(piece).toSet(), locked.bricks.toSet())
        assertEquals(next, locked.spirit)
        assertEquals(0, locked.line)
    }

    @Test fun completingARowUpdatesBoardScoreLineAndLevelTogether() {
        val piece = Spirit(listOf(Offset(0, 0)), Offset(11, 23))
        given(running(piece).copy(bricks = Brick.of(0..10, 23..23), line = 19, score = 200))
        model.dispatch(Action.GameTick)
        val result = awaitState { it.line == 20 && it.isRuning }
        assertEquals(312, result.score)
        assertEquals(2, result.level)
        assertTrue(result.bricks.isEmpty())
    }

    @Test fun resetFromGameOverClearsCountersAndTheBoard() {
        given(running().copy(gameStatus = GameStatus.GameOver, score = 100, line = 20, bricks = Brick.of(0..11, 23..23)))
        model.dispatch(Action.Reset)
        val state = awaitState { it.isRuning }
        assertEquals(0, state.score)
        assertEquals(0, state.line)
        assertTrue(state.bricks.isEmpty())
        assertEquals(1, state.level)
    }
}
