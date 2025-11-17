package com.swedishvocab.utils

import android.content.Context
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.swedishvocab.R
import java.util.Locale

class AudioPlayer(private val context: Context) {

    private var textToSpeech: TextToSpeech? = null
    private var isInitialized = false
    private var isPlaying = false
    private var soundEffectPlayer: MediaPlayer? = null
    private var onInitializedListener: (() -> Unit)? = null

    init {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale("sv", "SE"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Swedish not available, try generic Swedish
                    textToSpeech?.setLanguage(Locale("sv"))
                }
                isInitialized = true
                onInitializedListener?.invoke()
            }
        }
    }

    fun setOnInitializedListener(listener: (() -> Unit)?) {
        onInitializedListener = listener
        // If already initialized, call immediately
        if (isInitialized) {
            listener?.invoke()
        }
    }

    fun speak(text: String, onComplete: (() -> Unit)? = null, onError: (() -> Unit)? = null) {
        if (!isInitialized) {
            onError?.invoke()
            return
        }

        textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                isPlaying = true
            }

            override fun onDone(utteranceId: String?) {
                isPlaying = false
                onComplete?.invoke()
            }

            override fun onError(utteranceId: String?) {
                isPlaying = false
                onError?.invoke()
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?, errorCode: Int) {
                isPlaying = false
                onError?.invoke()
            }
        })

        val params = HashMap<String, String>()
        params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "SWEDISH_WORD"

        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, params)
    }

    fun stop() {
        textToSpeech?.stop()
        isPlaying = false
    }

    fun release() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
        isPlaying = false
        isInitialized = false
        releaseSoundEffect()
    }

    fun isCurrentlyPlaying(): Boolean = isPlaying

    fun isReady(): Boolean = isInitialized

    /**
     * Play a sound effect for correct answer
     */
    fun playCorrectSound(onComplete: (() -> Unit)? = null) {
        playSoundEffect(R.raw.sound_correct, onComplete)
    }

    /**
     * Play a sound effect for incorrect answer
     */
    fun playIncorrectSound(onComplete: (() -> Unit)? = null) {
        playSoundEffect(R.raw.sound_incorrect, onComplete)
    }

    private fun playSoundEffect(resourceId: Int, onComplete: (() -> Unit)? = null) {
        try {
            // Release previous sound effect if any
            releaseSoundEffect()

            soundEffectPlayer = MediaPlayer.create(context, resourceId)
            soundEffectPlayer?.setOnCompletionListener {
                releaseSoundEffect()
                onComplete?.invoke()
            }
            soundEffectPlayer?.setOnErrorListener { _, _, _ ->
                releaseSoundEffect()
                false
            }
            soundEffectPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
            releaseSoundEffect()
        }
    }

    private fun releaseSoundEffect() {
        soundEffectPlayer?.release()
        soundEffectPlayer = null
    }
}
