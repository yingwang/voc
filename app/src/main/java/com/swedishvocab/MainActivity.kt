package com.swedishvocab

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.swedishvocab.data.DictionaryLoader
import com.swedishvocab.utils.PreferencesManager
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var preferencesManager: PreferencesManager
    private lateinit var dictionaryLoader: DictionaryLoader
    private lateinit var startQuizButton: MaterialButton
    private lateinit var highScoreText: TextView
    private lateinit var vocabularyInfoText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferencesManager = PreferencesManager(this)
        dictionaryLoader = DictionaryLoader(this)

        initializeViews()
        updateHighScore()
        updateVocabularyInfo()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        updateHighScore()
    }

    private fun initializeViews() {
        startQuizButton = findViewById(R.id.startQuizButton)
        highScoreText = findViewById(R.id.highScoreText)
        vocabularyInfoText = findViewById(R.id.vocabularyInfoText)
    }

    private fun updateHighScore() {
        val highScore = preferencesManager.highScore
        highScoreText.text = getString(R.string.high_score, highScore)
    }

    private fun updateVocabularyInfo() {
        val wordCount = dictionaryLoader.getAllWords().size
        vocabularyInfoText.text = getString(R.string.vocabulary_info, wordCount)
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
