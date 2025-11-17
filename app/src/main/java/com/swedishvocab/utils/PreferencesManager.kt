package com.swedishvocab.utils

import android.content.Context
import android.content.SharedPreferences
import com.swedishvocab.models.DifficultyLevel
import com.swedishvocab.models.HighScore

class PreferencesManager(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(
        "swedish_vocab_prefs",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val KEY_HIGH_SCORE = "high_score"
        private const val KEY_GAMES_PLAYED = "games_played"
        private const val KEY_TOTAL_SCORE = "total_score"
        private const val KEY_HIGH_SCORES_LIST = "high_scores_list"
        private const val KEY_DIFFICULTY = "difficulty"
        private const val KEY_QUESTION_COUNT = "question_count"
        private const val MAX_HIGH_SCORES = 5
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

    // Settings
    var difficulty: DifficultyLevel
        get() = DifficultyLevel.fromString(preferences.getString(KEY_DIFFICULTY, DifficultyLevel.ALL.name) ?: DifficultyLevel.ALL.name)
        set(value) = preferences.edit().putString(KEY_DIFFICULTY, value.name).apply()

    var questionCount: Int
        get() = preferences.getInt(KEY_QUESTION_COUNT, 30)
        set(value) = preferences.edit().putInt(KEY_QUESTION_COUNT, value).apply()

    // High Scores List
    fun addHighScore(score: Int, total: Int, difficulty: DifficultyLevel) {
        val highScore = HighScore(
            score = score,
            total = total,
            timestamp = System.currentTimeMillis(),
            difficulty = difficulty.name,
            questionCount = total
        )

        val scores = getHighScores().toMutableList()
        scores.add(highScore)

        // Sort by percentage descending, then by score descending, then by timestamp descending (newer first)
        scores.sortWith(
            compareByDescending<HighScore> { it.percentage }
                .thenByDescending { it.score }
                .thenByDescending { it.timestamp }
        )

        // Keep only top MAX_HIGH_SCORES
        val topScores = scores.take(MAX_HIGH_SCORES)

        // Save to preferences
        val scoresJson = topScores.joinToString(";") { it.toJson() }
        preferences.edit().putString(KEY_HIGH_SCORES_LIST, scoresJson).apply()
    }

    fun getHighScores(): List<HighScore> {
        val scoresJson = preferences.getString(KEY_HIGH_SCORES_LIST, "") ?: ""
        if (scoresJson.isEmpty()) return emptyList()

        return scoresJson.split(";")
            .mapNotNull { HighScore.fromJson(it) }
    }

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
