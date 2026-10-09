package com.jetgame.tetris

import androidx.compose.runtime.MutableState
import com.jetgame.tetris.logic.Action
import com.jetgame.tetris.logic.Brick
import com.jetgame.tetris.logic.Direction
import com.jetgame.tetris.logic.GameStatus
import com.jetgame.tetris.logic.GameViewModel
import com.jetgame.tetris.logic.GameViewModel.ViewState
import com.jetgame.tetris.logic.Offset
import com.jetgame.tetris.logic.Spirit
import com.jetgame.tetris.logic.SpiritType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * @brief Unit tests of the GameViewModel state machine (dispatch of actions).
 *
 * The view model works on background threads, therefore the tests wait for the expected
 * state with a polling helper. States are injected through reflection and are always muted,
 * so the Android SoundPool is never touched on the JVM.
 */
class GameViewModelTest {

    private lateinit var vm: GameViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        vm = GameViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /** @brief Replaces the private view state of the view model (test setup only). */
    private fun inject(state: ViewState) {
        val field = GameViewModel::class.java.getDeclaredField("_viewState")
        field.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        val holder = field.get(vm) as MutableState<ViewState>
        holder.value = state
    }

    /** @brief Creates a muted state with the given content. */
    private fun state(
        status: GameStatus = GameStatus.Running,
        spirit: Spirit = Spirit.Empty,
        bricks: List<Brick> = emptyList(),
        reserve: List<Spirit> = emptyList()
    ) = ViewState(
        bricks = bricks,
        spirit = spirit,
        spiritReserve = reserve,
        gameStatus = status,
        isMute = true
    )

    /** @brief Polls the view state until the predicate holds or the timeout expires. */
    private fun awaitState(timeoutMs: Long = 15_000, predicate: (ViewState) -> Boolean): ViewState {
        val deadline = System.currentTimeMillis() + timeoutMs
        while (System.currentTimeMillis() < deadline) {
            val current = vm.viewState.value
            if (predicate(current)) return current
            Thread.sleep(10)
        }
        throw AssertionError("Timed out waiting for state, last state: ${vm.viewState.value}")
    }

    /** @brief Gives the background reducer time to finish when no state change is expected. */
    private fun settle() = Thread.sleep(300)

    /**
     * @brief Reset on the start screen starts a new game with zero values.
     * @testcase F-002, F-003
     * @technique ST
     */
    @Test
    fun F002_resetFromOnboardStartsNewGame() {
        assertEquals(GameStatus.Onboard, vm.viewState.value.gameStatus)
        vm.dispatch(Action.Reset)
        val started = awaitState { it.gameStatus == GameStatus.Running }
        assertEquals(0, started.score)
        assertEquals(0, started.line)
        assertEquals(1, started.level)
        assertTrue(started.bricks.isEmpty())
    }

    /**
     * @brief The mute action toggles the mute flag on and off.
     * @testcase F-042, F-043
     * @technique ST
     */
    @Test
    fun F042_F043_muteActionTogglesTheMuteFlag() {
        vm.dispatch(Action.Mute)
        awaitState { it.isMute }
        vm.dispatch(Action.Mute)
        val unmuted = awaitState { !it.isMute }
        assertFalse(unmuted.isMute)
    }

    /**
     * @brief Pause stops a running game.
     * @testcase F-004
     * @technique ST
     */
    @Test
    fun F004_pauseStopsRunningGame() {
        inject(state(spirit = spiritOf(TYPE_I, 5, 5)))
        vm.dispatch(Action.Pause)
        assertTrue(awaitState { it.isPaused }.isPaused)
    }

    /**
     * @brief Resume continues the game and keeps score, lines and board.
     * @testcase F-005
     * @technique ST
     */
    @Test
    fun F005_resumeKeepsBoardAndScore() {
        val paused = state(
            status = GameStatus.Paused,
            spirit = spiritOf(TYPE_T, 5, 8),
            bricks = rowBricks(23, 0..5)
        ).copy(score = 300, line = 3)
        inject(paused)
        vm.dispatch(Action.Resume)
        val resumed = awaitState { it.isRuning }
        assertEquals(paused.copy(gameStatus = GameStatus.Running), resumed)
    }

    /**
     * @brief Controls and ticks are ignored while the game is not running.
     * @testcase F-006, F-016
     * @technique ST
     */
    @Test
    fun F006_controlsAreIgnoredWhilePaused() {
        val paused = state(status = GameStatus.Paused, spirit = spiritOf(TYPE_T, 5, 8))
        inject(paused)
        val actions = listOf(
            Action.Move(Direction.Left),
            Action.Move(Direction.Right),
            Action.Move(Direction.Down),
            Action.Rotate,
            Action.Drop,
            Action.GameTick
        )
        for (action in actions) {
            vm.dispatch(action)
            settle()
            assertEquals(action.toString(), paused, vm.viewState.value)
        }
    }

    /**
     * @brief Drop lands on the floor or on the stack and a repeated drop changes nothing.
     * @testcase F-024, F-025, F-026
     * @technique EP
     */
    @Test
    fun F024_F026_dropLandsOnFloorAndOnStackAndIsIdempotent() {
        inject(state(spirit = spiritOf(TYPE_I, 5, 5)))
        vm.dispatch(Action.Drop)
        val floor = setOf(5 to 20, 5 to 21, 5 to 22, 5 to 23)
        awaitState { it.spirit.cells() == floor }
        vm.dispatch(Action.Drop)
        settle()
        assertEquals(floor, vm.viewState.value.spirit.cells())

        inject(state(spirit = spiritOf(TYPE_I, 5, 5), bricks = Brick.of(listOf(Offset(5, 15)))))
        vm.dispatch(Action.Drop)
        val onStack = setOf(5 to 11, 5 to 12, 5 to 13, 5 to 14)
        assertEquals(onStack, awaitState { it.spirit.cells() == onStack }.spirit.cells())
    }

    /**
     * @brief A tick moves the piece down; at the bottom it locks and gives 12 points.
     * @testcase F-027, F-035
     * @technique ST
     */
    @Test
    fun F027_F035_tickMovesPieceDownThenLocksItForTwelvePoints() {
        val next = spiritOf(TYPE_T, 5, 0)
        val afterNext = spiritOf(TYPE_O, 5, 0)
        inject(state(spirit = spiritOf(TYPE_I, 5, 5), reserve = listOf(next, afterNext)))
        vm.dispatch(Action.GameTick)
        val fell = setOf(5 to 5, 5 to 6, 5 to 7, 5 to 8)
        awaitState { it.spirit.cells() == fell }

        inject(state(spirit = spiritOf(TYPE_I, 5, 21), reserve = listOf(next, afterNext)))
        vm.dispatch(Action.GameTick)
        val locked = awaitState { it.score == 12 }
        assertEquals(0, locked.line)
        assertEquals(setOf(5 to 20, 5 to 21, 5 to 22, 5 to 23), locked.bricks.cells())
        assertEquals(next, locked.spirit)
        assertEquals(listOf(afterNext), locked.spiritReserve)
    }

    /**
     * @brief Locking a piece that completes one row gives 100 + 12 points and 1 line.
     * @testcase F-029
     * @technique ST
     */
    @Test
    fun F029_singleLineClearViaTickGivesHundredTwelvePoints() {
        val next = spiritOf(TYPE_T, 5, 0)
        val afterNext = spiritOf(TYPE_O, 5, 0)
        inject(
            state(
                spirit = spiritOf(TYPE_I, 11, 21),
                bricks = rowBricks(23, 0..10),
                reserve = listOf(next, afterNext)
            )
        )
        vm.dispatch(Action.GameTick)
        val cleared = awaitState { it.gameStatus == GameStatus.Running && it.line == 1 }
        assertEquals(112, cleared.score)
        assertEquals(1, cleared.level)
        assertEquals(setOf(11 to 21, 11 to 22, 11 to 23), cleared.bricks.cells())
        assertEquals(next, cleared.spirit)
    }

    /**
     * @brief A new piece that overlaps the stack ends the game.
     * @testcase F-048
     * @technique ST
     */
    @Test
    fun F048_overlappingNewPieceEndsTheGame() {
        inject(
            state(
                spirit = spiritOf(TYPE_I, 5, 0),
                bricks = Brick.of(listOf(Offset(5, 2)))
            )
        )
        vm.dispatch(Action.GameTick)
        assertEquals(GameStatus.GameOver, awaitState { it.gameStatus == GameStatus.GameOver }.gameStatus)
    }

    /**
     * @brief Reset after game over starts a clean game and keeps the mute flag.
     * @testcase F-049, F-044
     * @technique ST
     */
    @Test
    fun F049_F044_resetAfterGameOverStartsCleanGameAndKeepsMute() {
        inject(
            state(status = GameStatus.GameOver, bricks = rowBricks(23, 0..5))
                .copy(score = 500, line = 5)
        )
        vm.dispatch(Action.Reset)
        val fresh = awaitState { it.gameStatus == GameStatus.Running }
        assertEquals(0, fresh.score)
        assertEquals(0, fresh.line)
        assertTrue(fresh.bricks.isEmpty())
        assertTrue(fresh.isMute)
    }

    /**
     * @brief The ROTATE button action turns the active piece.
     * @testcase F-062
     * @technique EP
     */
    @Test
    fun F062_rotateButtonTurnsActiveSpirit() {
        inject(state(spirit = spiritOf(TYPE_T, 5, 10)))
        vm.dispatch(Action.Rotate)
        val expected = setOf(4 to 10, 5 to 10, 6 to 10, 5 to 9)
        assertEquals(expected, awaitState { it.spirit.cells() == expected }.spirit.cells())
    }

    /**
     * @brief The right arrow action moves the active piece one column to the right.
     * @testcase F-063
     * @technique EP
     */
    @Test
    fun F063_moveRightButtonShiftsActiveSpirit() {
        inject(state(spirit = spiritOf(TYPE_I, 5, 10)))
        vm.dispatch(Action.Move(Direction.Right))
        val expected = setOf(6 to 9, 6 to 10, 6 to 11, 6 to 12)
        assertEquals(expected, awaitState { it.spirit.cells() == expected }.spirit.cells())
    }

    /**
     * @brief The left arrow action at the left wall leaves the piece where it is.
     * @testcase F-063
     * @technique BVA
     */
    @Test
    fun F063_moveLeftIntoWallKeepsSpiritInPlace() {
        val atWall = state(spirit = spiritOf(TYPE_I, 0, 10))
        inject(atWall)
        vm.dispatch(Action.Move(Direction.Left))
        settle()
        assertEquals(atWall, vm.viewState.value)
    }

    /**
     * @brief Locking a piece that completes two rows gives 300 + 12 points and 2 lines.
     * @testcase F-066
     * @technique ST
     */
    @Test
    fun F066_twoLineClearViaTickGivesThreeHundredTwelvePoints() {
        val next = spiritOf(TYPE_T, 5, 0)
        val afterNext = spiritOf(TYPE_O, 5, 0)
        inject(
            state(
                spirit = spiritOf(TYPE_I, 11, 21),
                bricks = rowBricks(22, 0..10) + rowBricks(23, 0..10),
                reserve = listOf(next, afterNext)
            )
        )
        vm.dispatch(Action.GameTick)
        val cleared = awaitState { it.gameStatus == GameStatus.Running && it.line == 2 }
        assertEquals(312, cleared.score)
        assertEquals(setOf(11 to 22, 11 to 23), cleared.bricks.cells())
    }

    /**
     * @brief Locking a piece that completes four rows gives 1500 + 12 points and 4 lines.
     * @testcase F-066
     * @technique ST
     */
    @Test
    fun F066_fourLineClearViaTickGivesFifteenHundredTwelvePoints() {
        val next = spiritOf(TYPE_T, 5, 0)
        val afterNext = spiritOf(TYPE_O, 5, 0)
        inject(
            state(
                spirit = spiritOf(TYPE_I, 11, 21),
                bricks = (20..23).flatMap { rowBricks(it, 0..10) },
                reserve = listOf(next, afterNext)
            )
        )
        vm.dispatch(Action.GameTick)
        val cleared = awaitState { it.gameStatus == GameStatus.Running && it.line == 4 }
        assertEquals(1512, cleared.score)
        assertEquals(1, cleared.level)
        assertTrue(cleared.bricks.isEmpty())
    }

    /**
     * @brief Reset during a running game clears the board and the score and shows the start screen.
     * @testcase F-007, F-067
     * @technique ST
     */
    @Test
    fun F067_restartFromRunningClearsBoardAndScore() {
        inject(
            state(spirit = spiritOf(TYPE_I, 5, 5), bricks = rowBricks(23, 0..5))
                .copy(score = 500, line = 5)
        )
        vm.dispatch(Action.Reset)
        val restarted = awaitState { it.gameStatus == GameStatus.Onboard }
        assertEquals(0, restarted.score)
        assertEquals(0, restarted.line)
        assertTrue(restarted.bricks.isEmpty())
        assertTrue(restarted.isMute)
    }

    /**
     * @brief Reset during a paused game clears the board and the score and shows the start screen.
     * @testcase F-008, F-067
     * @technique ST
     */
    @Test
    fun F067_restartFromPausedClearsBoardAndScore() {
        inject(
            state(status = GameStatus.Paused, bricks = rowBricks(23, 0..5))
                .copy(score = 500, line = 5)
        )
        vm.dispatch(Action.Reset)
        val restarted = awaitState { it.gameStatus == GameStatus.Onboard }
        assertEquals(0, restarted.score)
        assertEquals(0, restarted.line)
        assertTrue(restarted.bricks.isEmpty())
    }

    /**
     * @brief Dropping any of the seven shapes on an empty board reaches the bottom row.
     * @testcase F-068
     * @technique EP
     */
    @Test
    fun F068_dropOfEverySevenShapesReachesTheBottomRow() {
        for (type in SpiritType.indices) {
            inject(state(spirit = spiritOf(type, 5, 5)))
            vm.dispatch(Action.Drop)
            val landed = awaitState { s -> s.spirit.cells().maxOf { it.second } == 23 }
            assertEquals("shape $type", 4, landed.spirit.cells().size)
        }
    }

    /**
     * @brief Pause on an already paused game does not change the state.
     * @testcase F-070
     * @technique ST
     */
    @Test
    fun F070_pauseWhenAlreadyPausedKeepsStateUnchanged() {
        val paused = state(status = GameStatus.Paused, spirit = spiritOf(TYPE_T, 5, 8))
        inject(paused)
        vm.dispatch(Action.Pause)
        settle()
        assertEquals(paused, vm.viewState.value)
    }

    /**
     * @brief Resume on an already running game does not change the state.
     * @testcase F-070
     * @technique ST
     */
    @Test
    fun F070_resumeWhenAlreadyRunningKeepsStateUnchanged() {
        val running = state(spirit = spiritOf(TYPE_T, 5, 8))
        inject(running)
        vm.dispatch(Action.Resume)
        settle()
        assertEquals(running, vm.viewState.value)
    }
}
