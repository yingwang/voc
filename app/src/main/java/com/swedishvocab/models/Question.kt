package com.swedishvocab.models

data class Question(
    val swedishWord: String,
    val correctAnswer: String,
    val options: List<String>,
    val category: String,
    val audioUrl: String? = null,
    val phonetic: String? = null
) {
    fun isCorrect(answer: String): Boolean {
        return answer.equals(correctAnswer, ignoreCase = true)
    }

    fun hasAudio(): Boolean {
        return !audioUrl.isNullOrEmpty()
    }
}
