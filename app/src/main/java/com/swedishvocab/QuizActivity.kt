package com.swedishvocab

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.swedishvocab.data.QuizManager
import com.swedishvocab.models.GameState
import com.swedishvocab.models.Question

class QuizActivity : AppCompatActivity() {

    private lateinit var quizManager: QuizManager
    private lateinit var gameState: GameState

    private lateinit var questionCounter: TextView
    private lateinit var scoreText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var swedishWord: TextView
    private lateinit var feedbackText: TextView
    private lateinit var optionButtons: List<MaterialButton>

    private var isAnswerSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        initializeViews()
        initializeQuiz()
        displayCurrentQuestion()
    }

    private fun initializeViews() {
        questionCounter = findViewById(R.id.questionCounter)
        scoreText = findViewById(R.id.scoreText)
        progressBar = findViewById(R.id.progressBar)
        swedishWord = findViewById(R.id.swedishWord)
        feedbackText = findViewById(R.id.feedbackText)

        optionButtons = listOf(
            findViewById(R.id.option1Button),
            findViewById(R.id.option2Button),
            findViewById(R.id.option3Button),
            findViewById(R.id.option4Button)
        )

        setupOptionClickListeners()
    }

    private fun initializeQuiz() {
        quizManager = QuizManager(this)
        gameState = quizManager.generateQuiz(30)
    }

    private fun setupOptionClickListeners() {
        optionButtons.forEach { button ->
            button.setOnClickListener {
                if (!isAnswerSelected) {
                    handleAnswerSelection(button.text.toString())
                }
            }
        }
    }

    private fun displayCurrentQuestion() {
        val question = gameState.currentQuestion ?: run {
            finishQuiz()
            return
        }

        isAnswerSelected = false
        feedbackText.visibility = View.GONE

        // Reset button styles
        resetButtonStyles()

        // Update UI
        questionCounter.text = getString(
            R.string.question_format,
            gameState.currentQuestionIndex + 1
        )
        scoreText.text = getString(R.string.score_format, gameState.score)
        progressBar.progress = gameState.progressPercentage
        swedishWord.text = question.swedishWord

        // Set option texts
        question.options.forEachIndexed { index, option ->
            if (index < optionButtons.size) {
                optionButtons[index].text = option
            }
        }
    }

    private fun handleAnswerSelection(selectedAnswer: String) {
        isAnswerSelected = true
        val currentQuestion = gameState.currentQuestion ?: return

        val isCorrect = currentQuestion.isCorrect(selectedAnswer)

        // Update game state
        gameState = quizManager.answerQuestion(gameState, selectedAnswer)

        // Show feedback
        showFeedback(isCorrect, currentQuestion, selectedAnswer)

        // Update button colors
        updateButtonColors(currentQuestion, selectedAnswer)

        // Move to next question after delay
        Handler(Looper.getMainLooper()).postDelayed({
            if (gameState.isComplete) {
                finishQuiz()
            } else {
                displayCurrentQuestion()
            }
        }, 1500)
    }

    private fun showFeedback(isCorrect: Boolean, question: Question, selectedAnswer: String) {
        feedbackText.visibility = View.VISIBLE

        if (isCorrect) {
            feedbackText.text = getString(R.string.correct)
            feedbackText.setTextColor(ContextCompat.getColor(this, R.color.correct))
        } else {
            feedbackText.text = getString(R.string.incorrect, question.correctAnswer)
            feedbackText.setTextColor(ContextCompat.getColor(this, R.color.incorrect))
        }
    }

    private fun updateButtonColors(question: Question, selectedAnswer: String) {
        optionButtons.forEach { button ->
            val buttonText = button.text.toString()

            when {
                buttonText.equals(question.correctAnswer, ignoreCase = true) -> {
                    // Correct answer - green
                    button.setBackgroundColor(ContextCompat.getColor(this, R.color.correct))
                    button.setTextColor(Color.WHITE)
                }
                buttonText.equals(selectedAnswer, ignoreCase = true) &&
                        !question.isCorrect(selectedAnswer) -> {
                    // Wrong selection - red
                    button.setBackgroundColor(ContextCompat.getColor(this, R.color.incorrect))
                    button.setTextColor(Color.WHITE)
                }
            }

            button.isEnabled = false
        }
    }

    private fun resetButtonStyles() {
        optionButtons.forEach { button ->
            button.setBackgroundColor(Color.TRANSPARENT)
            button.setTextColor(ContextCompat.getColor(this, R.color.text_primary))
            button.isEnabled = true
        }
    }

    private fun finishQuiz() {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("SCORE", gameState.score)
            putExtra("TOTAL", gameState.totalQuestions)
        }
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        // Prevent back button during quiz
        // User must complete the quiz
    }
}
