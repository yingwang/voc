package com.swedishvocab.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale

class AudioPlayer(context: Context) {

    private var textToSpeech: TextToSpeech? = null
    private var isInitialized = false
    private var isPlaying = false

    init {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale("sv", "SE"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Swedish not available, try generic Swedish
                    textToSpeech?.setLanguage(Locale("sv"))
                }
                isInitialized = true
            }
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
    }

    fun isCurrentlyPlaying(): Boolean = isPlaying

    fun isReady(): Boolean = isInitialized
}
