package com.swedishvocab

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.swedishvocab.data.DictionaryLoader
import com.swedishvocab.utils.PreferencesManager
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var preferencesManager: PreferencesManager
    private lateinit var dictionaryLoader: DictionaryLoader
    private lateinit var startQuizButton: MaterialButton
    private lateinit var settingsButton: MaterialButton
    private lateinit var vocabularyInfoText: TextView
    private lateinit var currentSettingsText: TextView
    private lateinit var highScoresList: LinearLayout
    private lateinit var noScoresText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferencesManager = PreferencesManager(this)
        dictionaryLoader = DictionaryLoader(this)

        initializeViews()
        updateHighScoresList()
        updateCurrentSettings()
        updateVocabularyInfo()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        updateHighScoresList()
        updateCurrentSettings()
    }

    private fun initializeViews() {
        startQuizButton = findViewById(R.id.startQuizButton)
        settingsButton = findViewById(R.id.settingsButton)
        vocabularyInfoText = findViewById(R.id.vocabularyInfoText)
        currentSettingsText = findViewById(R.id.currentSettingsText)
        highScoresList = findViewById(R.id.highScoresList)
        noScoresText = findViewById(R.id.noScoresText)
    }

    private fun updateHighScoresList() {
        val highScores = preferencesManager.getHighScores()

        highScoresList.removeAllViews()

        if (highScores.isEmpty()) {
            noScoresText.visibility = View.VISIBLE
            highScoresList.visibility = View.GONE
        } else {
            noScoresText.visibility = View.GONE
            highScoresList.visibility = View.VISIBLE

            highScores.forEach { score ->
                val scoreView = TextView(this).apply {
                    text = getString(
                        R.string.score_entry,
                        score.score,
                        score.total,
                        score.percentage,
                        score.getFormattedDate()
                    )
                    textSize = 14f
                    setPadding(0, 8, 0, 8)
                    setTextColor(getColor(R.color.text_primary))
                }
                highScoresList.addView(scoreView)
            }
        }
    }

    private fun updateCurrentSettings() {
        val difficulty = preferencesManager.difficulty
        val questionCount = preferencesManager.questionCount
        currentSettingsText.text = "$questionCount questions • ${difficulty.displayName}"
    }

    private fun updateVocabularyInfo() {
        val wordCount = dictionaryLoader.getAllWords().size
        vocabularyInfoText.text = getString(R.string.vocabulary_info, wordCount)
    }

    private fun setupClickListeners() {
        startQuizButton.setOnClickListener {
            startQuiz()
        }

        settingsButton.setOnClickListener {
            openSettings()
        }
    }

    private fun startQuiz() {
        val intent = Intent(this, QuizActivity::class.java)
        startActivity(intent)
    }

    private fun openSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}
