package com.jetgame.tetris.logic

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ScoreTest(private val lines: Int, private val expected: Int) {
    @Test fun lineScoreMatchesThePublishedGameRules() = assertEquals(expected, calculateScore(lines))
    companion object {
        @JvmStatic @Parameterized.Parameters(name = "{0} lines -> {1} points")
        fun cases() = listOf(arrayOf(0, 0), arrayOf(1, 100), arrayOf(2, 300), arrayOf(3, 700), arrayOf(4, 1500))
    }
}
