package com.swedishvocab.models

data class GameState(
    val questions: List<Question>,
    val currentQuestionIndex: Int = 0,
    val score: Int = 0,
    val answeredQuestions: Int = 0
) {
    val totalQuestions: Int
        get() = questions.size
    val currentQuestion: Question?
        get() = if (currentQuestionIndex < questions.size) questions[currentQuestionIndex] else null
    val isComplete: Boolean
        get() = answeredQuestions >= totalQuestions
    val progressPercentage: Int
        get() = if (totalQuestions > 0) (answeredQuestions * 100) / totalQuestions else 0
}
