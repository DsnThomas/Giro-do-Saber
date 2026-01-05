package com.thomasdias.quizroleta

import android.content.Context
import android.media.MediaPlayer

object MusicManager {
    private var mediaPlayer: MediaPlayer? = null
    private var isMuted = false

    fun iniciarMusica(context: Context) {
        // Só cria se ainda não existir
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.musica_fundo)
            mediaPlayer?.isLooping = true // Para repetir infinitamente
            mediaPlayer?.start()
        }
    }

    fun pausar() {
        mediaPlayer?.pause()
    }

    fun retomar() {
        if (!isMuted && mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }
    }

    fun isTocando(): Boolean {
        return !isMuted
    }

    fun alternarSom(): Boolean {
        isMuted = !isMuted
        if (isMuted) {
            mediaPlayer?.setVolume(0f, 0f) // Volume zero
        } else {
            mediaPlayer?.setVolume(1f, 1f) // Volume máximo
        }
        return isMuted // Retorna o estado novo (se está mudo ou não)
    }
    fun pararMusica() {
        try {
            if (mediaPlayer != null) {
                if (mediaPlayer!!.isPlaying) {
                    mediaPlayer!!.stop()
                }
                mediaPlayer!!.release()
                mediaPlayer = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}