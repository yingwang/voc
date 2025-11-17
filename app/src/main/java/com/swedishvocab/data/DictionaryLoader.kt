package com.swedishvocab.data

import android.content.Context
import com.google.gson.Gson
import com.swedishvocab.R
import com.swedishvocab.models.Dictionary
import com.swedishvocab.models.Word
import java.io.InputStreamReader

class DictionaryLoader(private val context: Context) {

    private var dictionary: Dictionary? = null

    fun loadDictionary(): Dictionary {
        if (dictionary != null) {
            return dictionary!!
        }

        val inputStream = context.resources.openRawResource(R.raw.dictionary)
        val reader = InputStreamReader(inputStream)

        dictionary = Gson().fromJson(reader, Dictionary::class.java)
        reader.close()

        return dictionary!!
    }

    fun getAllWords(): List<Word> {
        return loadDictionary().words
    }

    fun getRandomWords(count: Int): List<Word> {
        val allWords = getAllWords()
        return allWords.shuffled().take(count)
    }

    fun getWordsByCategory(category: String): List<Word> {
        return getAllWords().filter { it.category == category }
    }
}
