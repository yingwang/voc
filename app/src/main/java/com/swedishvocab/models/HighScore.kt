package com.swedishvocab.models

data class HighScore(
    val score: Int,
    val total: Int,
    val timestamp: Long,
    val difficulty: String,
    val questionCount: Int
) {
    val percentage: Int
        get() = (score * 100) / total

    fun getFormattedDate(): String {
        val sdf = java.text.SimpleDateFormat("MMM dd, yyyy HH:mm", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }

    companion object {
        fun fromJson(json: String): HighScore? {
            return try {
                val parts = json.split("|")
                if (parts.size >= 5) {
                    HighScore(
                        score = parts[0].toInt(),
                        total = parts[1].toInt(),
                        timestamp = parts[2].toLong(),
                        difficulty = parts[3],
                        questionCount = parts[4].toInt()
                    )
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    fun toJson(): String {
        return "$score|$total|$timestamp|$difficulty|$questionCount"
    }
}

enum class DifficultyLevel(val displayName: String, val maxWords: Int) {
    BEGINNER("Beginner", 3000),
    INTERMEDIATE("Intermediate", 10000),
    ADVANCED("Advanced", 20000),
    ALL("All", Int.MAX_VALUE);

    companion object {
        fun fromString(value: String): DifficultyLevel {
            return values().find { it.name == value } ?: ALL
        }
    }
}
