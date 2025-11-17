package com.swedishvocab.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(
        "swedish_vocab_prefs",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val KEY_HIGH_SCORE = "high_score"
        private const val KEY_GAMES_PLAYED = "games_played"
        private const val KEY_TOTAL_SCORE = "total_score"
    }

    var highScore: Int
        get() = preferences.getInt(KEY_HIGH_SCORE, 0)
        set(value) = preferences.edit().putInt(KEY_HIGH_SCORE, value).apply()

    var gamesPlayed: Int
        get() = preferences.getInt(KEY_GAMES_PLAYED, 0)
        set(value) = preferences.edit().putInt(KEY_GAMES_PLAYED, value).apply()

    var totalScore: Int
        get() = preferences.getInt(KEY_TOTAL_SCORE, 0)
        set(value) = preferences.edit().putInt(KEY_TOTAL_SCORE, value).apply()

    fun updateScore(newScore: Int): Boolean {
        val isNewHighScore = newScore > highScore

        if (isNewHighScore) {
            highScore = newScore
        }

        gamesPlayed++
        totalScore += newScore

        return isNewHighScore
    }

    fun getAverageScore(): Double {
        return if (gamesPlayed > 0) {
            totalScore.toDouble() / gamesPlayed
        } else {
            0.0
        }
    }

    fun reset() {
        preferences.edit().clear().apply()
    }
}
