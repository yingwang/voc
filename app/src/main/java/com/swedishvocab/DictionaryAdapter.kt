package com.swedishvocab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.swedishvocab.models.Word
import com.swedishvocab.utils.AudioPlayer

class DictionaryAdapter(
    private val audioPlayer: AudioPlayer,
    private val onWordClick: (Word) -> Unit
) : RecyclerView.Adapter<DictionaryAdapter.WordViewHolder>() {

    private var words: List<Word> = emptyList()
    private var playingPosition: Int = -1

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val swedishText: TextView = itemView.findViewById(R.id.swedishText)
        val phoneticText: TextView = itemView.findViewById(R.id.phoneticText)
        val englishText: TextView = itemView.findViewById(R.id.englishText)
        val categoryText: TextView = itemView.findViewById(R.id.categoryText)
        val playAudioButton: MaterialButton = itemView.findViewById(R.id.playAudioButton)

        fun bind(word: Word, position: Int) {
            swedishText.text = word.swedish
            englishText.text = word.english
            categoryText.text = word.category

            // Show phonetic if available
            if (!word.phonetic.isNullOrEmpty()) {
                phoneticText.text = "[${word.phonetic}]"
                phoneticText.visibility = View.VISIBLE
            } else {
                phoneticText.visibility = View.GONE
            }

            // Update play button state
            updatePlayButtonState(position)

            // Setup audio playback
            playAudioButton.setOnClickListener {
                playAudio(word, position)
            }

            itemView.setOnClickListener {
                onWordClick(word)
            }
        }

        private fun updatePlayButtonState(position: Int) {
            if (position == playingPosition && audioPlayer.isCurrentlyPlaying()) {
                playAudioButton.isEnabled = false
            } else {
                playAudioButton.isEnabled = audioPlayer.isReady()
            }
        }

        private fun playAudio(word: Word, position: Int) {
            // Stop any currently playing audio
            if (playingPosition != -1) {
                audioPlayer.stop()
                notifyItemChanged(playingPosition)
            }

            playingPosition = position
            playAudioButton.isEnabled = false

            audioPlayer.speak(
                text = word.swedish,
                onComplete = {
                    playingPosition = -1
                    notifyItemChanged(position)
                },
                onError = {
                    playingPosition = -1
                    notifyItemChanged(position)
                }
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dictionary_word, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(words[position], position)
    }

    override fun getItemCount(): Int = words.size

    fun updateWords(newWords: List<Word>) {
        // Stop any playing audio when list changes
        if (playingPosition != -1) {
            audioPlayer.stop()
            playingPosition = -1
        }

        words = newWords
        notifyDataSetChanged()
    }
}
