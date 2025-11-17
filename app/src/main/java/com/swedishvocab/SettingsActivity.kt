package com.swedishvocab

import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.swedishvocab.models.DifficultyLevel
import com.swedishvocab.utils.PreferencesManager

class SettingsActivity : AppCompatActivity() {

    private lateinit var preferencesManager: PreferencesManager
    private lateinit var difficultyRadioGroup: RadioGroup
    private lateinit var questionCountRadioGroup: RadioGroup
    private lateinit var saveButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        preferencesManager = PreferencesManager(this)

        initializeViews()
        loadCurrentSettings()
        setupClickListeners()
    }

    private fun initializeViews() {
        difficultyRadioGroup = findViewById(R.id.difficultyRadioGroup)
        questionCountRadioGroup = findViewById(R.id.questionCountRadioGroup)
        saveButton = findViewById(R.id.saveButton)
    }

    private fun loadCurrentSettings() {
        // Load difficulty
        val currentDifficulty = preferencesManager.difficulty
        when (currentDifficulty) {
            DifficultyLevel.BEGINNER -> findViewById<RadioButton>(R.id.difficultyBeginner).isChecked = true
            DifficultyLevel.INTERMEDIATE -> findViewById<RadioButton>(R.id.difficultyIntermediate).isChecked = true
            DifficultyLevel.ADVANCED -> findViewById<RadioButton>(R.id.difficultyAdvanced).isChecked = true
            DifficultyLevel.ALL -> findViewById<RadioButton>(R.id.difficultyAll).isChecked = true
        }

        // Load question count
        val currentCount = preferencesManager.questionCount
        when (currentCount) {
            10 -> findViewById<RadioButton>(R.id.questions10).isChecked = true
            20 -> findViewById<RadioButton>(R.id.questions20).isChecked = true
            30 -> findViewById<RadioButton>(R.id.questions30).isChecked = true
            50 -> findViewById<RadioButton>(R.id.questions50).isChecked = true
            else -> findViewById<RadioButton>(R.id.questions30).isChecked = true
        }
    }

    private fun setupClickListeners() {
        saveButton.setOnClickListener {
            saveSettings()
        }
    }

    private fun saveSettings() {
        // Save difficulty
        val difficulty = when (difficultyRadioGroup.checkedRadioButtonId) {
            R.id.difficultyBeginner -> DifficultyLevel.BEGINNER
            R.id.difficultyIntermediate -> DifficultyLevel.INTERMEDIATE
            R.id.difficultyAdvanced -> DifficultyLevel.ADVANCED
            R.id.difficultyAll -> DifficultyLevel.ALL
            else -> DifficultyLevel.ALL
        }
        preferencesManager.difficulty = difficulty

        // Save question count
        val questionCount = when (questionCountRadioGroup.checkedRadioButtonId) {
            R.id.questions10 -> 10
            R.id.questions20 -> 20
            R.id.questions30 -> 30
            R.id.questions50 -> 50
            else -> 30
        }
        preferencesManager.questionCount = questionCount

        Toast.makeText(this, R.string.settings_saved, Toast.LENGTH_SHORT).show()
        finish()
    }
}
