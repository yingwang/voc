package com.swedishvocab

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.swedishvocab.data.DictionaryLoader
import com.swedishvocab.models.Word
import com.swedishvocab.utils.AudioPlayer

class DictionaryActivity : AppCompatActivity() {

    private lateinit var dictionaryLoader: DictionaryLoader
    private lateinit var audioPlayer: AudioPlayer
    private lateinit var searchEditText: EditText
    private lateinit var clearSearchButton: MaterialButton
    private lateinit var wordCountText: TextView
    private lateinit var dictionaryRecyclerView: RecyclerView
    private lateinit var emptyStateText: TextView
    private lateinit var backButton: MaterialButton

    private var allWords: List<Word> = emptyList()
    private var filteredWords: List<Word> = emptyList()
    private lateinit var adapter: DictionaryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dictionary)

        initializeViews()
        initializeData()
        setupSearch()
        setupRecyclerView()
        displayWords(allWords)
    }

    private fun initializeViews() {
        searchEditText = findViewById(R.id.searchEditText)
        clearSearchButton = findViewById(R.id.clearSearchButton)
        wordCountText = findViewById(R.id.wordCountText)
        dictionaryRecyclerView = findViewById(R.id.dictionaryRecyclerView)
        emptyStateText = findViewById(R.id.emptyStateText)
        backButton = findViewById(R.id.backButton)

        backButton.setOnClickListener {
            finish()
        }

        clearSearchButton.setOnClickListener {
            searchEditText.text.clear()
        }
    }

    private fun initializeData() {
        dictionaryLoader = DictionaryLoader(this)
        audioPlayer = AudioPlayer(this)
        allWords = dictionaryLoader.getAllWords()
        filteredWords = allWords

        updateWordCount(allWords.size)
    }

    private fun setupSearch() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                clearSearchButton.visibility = if (query.isEmpty()) View.GONE else View.VISIBLE
                filterWords(query)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupRecyclerView() {
        adapter = DictionaryAdapter(audioPlayer) { word ->
            // Optional: handle word click
        }
        dictionaryRecyclerView.layoutManager = LinearLayoutManager(this)
        dictionaryRecyclerView.adapter = adapter
    }

    private fun filterWords(query: String) {
        filteredWords = if (query.isEmpty()) {
            allWords
        } else {
            allWords.filter { word ->
                word.swedish.contains(query, ignoreCase = true) ||
                        word.english.contains(query, ignoreCase = true)
            }
        }
        displayWords(filteredWords)
        updateWordCount(filteredWords.size)
    }

    private fun displayWords(words: List<Word>) {
        if (words.isEmpty()) {
            dictionaryRecyclerView.visibility = View.GONE
            emptyStateText.visibility = View.VISIBLE
        } else {
            dictionaryRecyclerView.visibility = View.VISIBLE
            emptyStateText.visibility = View.GONE
            adapter.updateWords(words)
        }
    }

    private fun updateWordCount(count: Int) {
        wordCountText.text = if (count == allWords.size) {
            "$count words"
        } else {
            "$count of ${allWords.size} words"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayer.release()
    }
}
