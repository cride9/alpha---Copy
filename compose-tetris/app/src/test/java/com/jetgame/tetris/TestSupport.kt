package com.jetgame.tetris

import com.jetgame.tetris.logic.Brick
import com.jetgame.tetris.logic.GameViewModel
import com.jetgame.tetris.logic.Spirit
import com.jetgame.tetris.logic.SpiritType

/**
 * @brief Shared helpers for the local JUnit tests of Compose Tetris.
 *
 * Naming convention of the tests: F<three digit ID>_<descriptiveName>.
 * The ID is the test case ID from the test specification (docs/Compose_Tetris_Tesztesetek.md,
 * F-001 .. F-070), so every JUnit test can be traced back to a planned functional test case.
 * Tests that cover a range use both IDs (for example F029_F032_...).
 * Every test has a KDoc block with @brief (what is checked), @testcase (ID) and
 * @technique (EP: equivalence partitioning, BVA: boundary value analysis, ST: state transition,
 * UC: use case, EB: experience based).
 *
 * The compose Offset value class stores floats, so -0.0f and 0.0f are different values.
 * To avoid false failures all board comparisons are done on integer (x, y) cell sets.
 */

/** @brief Board size used by the game (columns to rows). */
val MATRIX: Pair<Int, Int> = 12 to 24

/** @brief Indexes of the seven shapes in [SpiritType]. */
const val TYPE_Z = 0
const val TYPE_S = 1
const val TYPE_I = 2
const val TYPE_T = 3
const val TYPE_O = 4
const val TYPE_L = 5
const val TYPE_J = 6

/** @brief Cells occupied by the spirit as integer (x, y) pairs. */
fun Spirit.cells(): Set<Pair<Int, Int>> =
    location.map { it.x.toInt() to it.y.toInt() }.toSet()

/** @brief Cells occupied by the bricks as integer (x, y) pairs. */
fun List<Brick>.cells(): Set<Pair<Int, Int>> =
    map { it.location.x.toInt() to it.location.y.toInt() }.toSet()

/** @brief Creates a spirit of the given shape index at the given offset. */
fun spiritOf(type: Int, x: Int, y: Int): Spirit =
    Spirit(SpiritType[type], com.jetgame.tetris.logic.Offset(x, y))

/** @brief Creates a horizontal row of bricks in row [y] for the given columns. */
fun rowBricks(y: Int, xs: IntRange): List<Brick> = Brick.of(xs, y..y)

/** @brief Result of the private GameViewModel.updateBricks() call. */
class ClearResult(
    val before: List<Brick>,
    val clearing: List<Brick>,
    val cleared: List<Brick>,
    val lines: Int
)

/**
 * @brief Calls the private GameViewModel.updateBricks() through reflection.
 *
 * This is the line clearing algorithm of the game; it is private, so reflection is the only
 * way to unit test it without changing the production code.
 */
fun callUpdateBricks(
    vm: GameViewModel,
    bricks: List<Brick>,
    spirit: Spirit,
    matrix: Pair<Int, Int> = MATRIX
): ClearResult {
    val method = GameViewModel::class.java.getDeclaredMethod(
        "updateBricks", List::class.java, Spirit::class.java, Pair::class.java
    )
    method.isAccessible = true
    val result = method.invoke(vm, bricks, spirit, matrix) as Pair<*, *>
    val triple = result.first as Triple<*, *, *>
    @Suppress("UNCHECKED_CAST")
    return ClearResult(
        triple.first as List<Brick>,
        triple.second as List<Brick>,
        triple.third as List<Brick>,
        result.second as Int
    )
}
