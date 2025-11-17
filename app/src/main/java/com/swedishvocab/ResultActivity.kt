package com.swedishvocab

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.swedishvocab.utils.PreferencesManager

class ResultActivity : AppCompatActivity() {

    private lateinit var preferencesManager: PreferencesManager

    private lateinit var celebrationIcon: TextView
    private lateinit var finalScore: TextView
    private lateinit var percentageText: TextView
    private lateinit var highScoreBadge: TextView
    private lateinit var highScoreText: TextView
    private lateinit var performanceMessage: TextView
    private lateinit var playAgainButton: MaterialButton
    private lateinit var exitButton: MaterialButton

    private var score: Int = 0
    private var total: Int = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        preferencesManager = PreferencesManager(this)

        retrieveScore()
        initializeViews()
        displayResults()
        setupClickListeners()
    }

    private fun retrieveScore() {
        score = intent.getIntExtra("SCORE", 0)
        total = intent.getIntExtra("TOTAL", 30)
    }

    private fun initializeViews() {
        celebrationIcon = findViewById(R.id.celebrationIcon)
        finalScore = findViewById(R.id.finalScore)
        percentageText = findViewById(R.id.percentageText)
        highScoreBadge = findViewById(R.id.highScoreBadge)
        highScoreText = findViewById(R.id.highScoreText)
        performanceMessage = findViewById(R.id.performanceMessage)
        playAgainButton = findViewById(R.id.playAgainButton)
        exitButton = findViewById(R.id.exitButton)
    }

    private fun displayResults() {
        // Display score
        finalScore.text = "$score / $total"

        // Calculate and display percentage
        val percentage = if (total > 0) (score * 100) / total else 0
        percentageText.text = "$percentage%"

        // Add to high scores list
        val difficulty = preferencesManager.difficulty
        preferencesManager.addHighScore(score, total, difficulty)

        // Update old high score (for backward compatibility)
        val isNewHighScore = preferencesManager.updateScore(score)
        if (isNewHighScore) {
            highScoreBadge.visibility = View.VISIBLE
            celebrationIcon.text = "🏆"
        }

        // Show high score in appropriate format
        highScoreText.text = getString(R.string.high_score, preferencesManager.highScore)

        // Set performance message
        performanceMessage.text = getPerformanceMessage(percentage)
    }

    private fun getPerformanceMessage(percentage: Int): String {
        return when {
            percentage >= 90 -> "Outstanding! You're mastering Swedish! 🌟"
            percentage >= 75 -> "Excellent work! Keep practicing! 💪"
            percentage >= 60 -> "Good job! You're making great progress! 👍"
            percentage >= 50 -> "Not bad! Keep studying to improve! 📚"
            else -> "Keep practicing! You'll get better! 🎯"
        }
    }

    private fun setupClickListeners() {
        playAgainButton.setOnClickListener {
            playAgain()
        }

        exitButton.setOnClickListener {
            exitToHome()
        }
    }

    private fun playAgain() {
        val intent = Intent(this, QuizActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun exitToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        // Go back to main screen instead of quiz
        exitToHome()
    }
}
