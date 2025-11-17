package com.swedishvocab

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.swedishvocab.utils.PreferencesManager
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var preferencesManager: PreferencesManager
    private lateinit var startQuizButton: MaterialButton
    private lateinit var highScoreText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferencesManager = PreferencesManager(this)

        initializeViews()
        updateHighScore()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        updateHighScore()
    }

    private fun initializeViews() {
        startQuizButton = findViewById(R.id.startQuizButton)
        highScoreText = findViewById(R.id.highScoreText)
    }

    private fun updateHighScore() {
        val highScore = preferencesManager.highScore
        highScoreText.text = getString(R.string.high_score, highScore)
    }

    private fun setupClickListeners() {
        startQuizButton.setOnClickListener {
            startQuiz()
        }
    }

    private fun startQuiz() {
        val intent = Intent(this, QuizActivity::class.java)
        startActivity(intent)
    }
}
