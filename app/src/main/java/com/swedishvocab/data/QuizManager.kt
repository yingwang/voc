package com.swedishvocab.data

import android.content.Context
import com.swedishvocab.models.GameState
import com.swedishvocab.models.Question
import com.swedishvocab.models.Word

class QuizManager(context: Context) {

    private val dictionaryLoader = DictionaryLoader(context)

    fun generateQuiz(questionCount: Int = 30): GameState {
        val allWords = dictionaryLoader.getAllWords()

        if (allWords.size < 4) {
            throw IllegalStateException("Dictionary must have at least 4 words")
        }

        val selectedWords = allWords.shuffled().take(questionCount)
        val questions = selectedWords.map { word ->
            generateQuestion(word, allWords)
        }

        return GameState(questions = questions)
    }

    private fun generateQuestion(correctWord: Word, allWords: List<Word>): Question {
        val incorrectOptions = allWords
            .filter { it.english != correctWord.english }
            .shuffled()
            .take(3)
            .map { it.english }

        val allOptions = (incorrectOptions + correctWord.english).shuffled()

        return Question(
            swedishWord = correctWord.swedish,
            correctAnswer = correctWord.english,
            options = allOptions,
            category = correctWord.category
        )
    }

    fun answerQuestion(gameState: GameState, selectedAnswer: String): GameState {
        val currentQuestion = gameState.currentQuestion ?: return gameState

        val isCorrect = currentQuestion.isCorrect(selectedAnswer)
        val newScore = if (isCorrect) gameState.score + 1 else gameState.score

        return gameState.copy(
            currentQuestionIndex = gameState.currentQuestionIndex + 1,
            score = newScore,
            answeredQuestions = gameState.answeredQuestions + 1
        )
    }
}
