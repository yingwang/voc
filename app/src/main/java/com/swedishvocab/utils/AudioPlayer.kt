package com.swedishvocab.utils

import android.media.AudioAttributes
import android.media.MediaPlayer
import java.io.IOException

class AudioPlayer {

    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false

    fun playFromUrl(url: String, onComplete: (() -> Unit)? = null, onError: (() -> Unit)? = null) {
        // Release any existing player
        release()

        try {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )

                setDataSource(url)

                setOnPreparedListener {
                    start()
                    this@AudioPlayer.isPlaying = true
                }

                setOnCompletionListener {
                    this@AudioPlayer.isPlaying = false
                    onComplete?.invoke()
                    this@AudioPlayer.release()
                }

                setOnErrorListener { _, what, extra ->
                    this@AudioPlayer.isPlaying = false
                    onError?.invoke()
                    this@AudioPlayer.release()
                    true
                }

                prepareAsync()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            onError?.invoke()
            this.release()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            onError?.invoke()
            this.release()
        }
    }

    fun stop() {
        try {
            mediaPlayer?.apply {
                if (isPlaying()) {
                    stop()
                }
            }
            isPlaying = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun release() {
        try {
            mediaPlayer?.apply {
                stop()
                release()
            }
            mediaPlayer = null
            isPlaying = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isCurrentlyPlaying(): Boolean = isPlaying
}
