package com.swedishvocab.data

import android.content.Context
import com.swedishvocab.models.DifficultyLevel
import com.swedishvocab.models.GameState
import com.swedishvocab.models.Question
import com.swedishvocab.models.Word

class QuizManager(context: Context) {

    private val dictionaryLoader = DictionaryLoader(context)

    fun generateQuiz(questionCount: Int = 30, difficulty: DifficultyLevel = DifficultyLevel.ALL): GameState {
        val allWords = dictionaryLoader.getAllWords()

        if (allWords.size < 4) {
            throw IllegalStateException("Dictionary must have at least 4 words")
        }

        // Filter words based on difficulty (take first N words which are sorted by frequency)
        val filteredWords = when (difficulty) {
            DifficultyLevel.BEGINNER -> allWords.take(difficulty.maxWords.coerceAtMost(allWords.size))
            DifficultyLevel.INTERMEDIATE -> allWords.take(difficulty.maxWords.coerceAtMost(allWords.size))
            DifficultyLevel.ADVANCED -> allWords.take(difficulty.maxWords.coerceAtMost(allWords.size))
            DifficultyLevel.ALL -> allWords
        }

        val selectedWords = filteredWords.shuffled().take(questionCount)
        val questions = selectedWords.map { word ->
            // Use filtered words for generating incorrect options too
            generateQuestion(word, filteredWords)
        }

        return GameState(questions = questions)
    }

    private fun generateQuestion(correctWord: Word, allWords: List<Word>): Question {
        // Get 3 random incorrect options
        val incorrectOptions = allWords
            .filter { it.english != correctWord.english }
            .shuffled()
            .take(3)
            .map { it.english }

        // Shuffle all 4 options (3 incorrect + 1 correct) for randomness
        val allOptions = (incorrectOptions + correctWord.english).shuffled()

        return Question(
            swedishWord = correctWord.swedish,
            correctAnswer = correctWord.english,
            options = allOptions,
            category = correctWord.category,
            audioUrl = correctWord.audioUrl,
            phonetic = correctWord.phonetic
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
