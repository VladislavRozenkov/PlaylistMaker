package com.practicum.playlistmaker.player.data.repository

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.repository.AudioPlayerRepository

class AudioPlayerRepositoryImpl : AudioPlayerRepository {

    private val mediaPlayer = MediaPlayer()

    override fun prepare(
        url: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit,
        onError: () -> Unit
    ) {
        try {
            mediaPlayer.setDataSource(url)

            mediaPlayer.setOnPreparedListener {
                onPrepared()
            }

            mediaPlayer.setOnCompletionListener {
                onCompletion()
            }

            mediaPlayer.setOnErrorListener { _, _, _ ->
                onError()
                true
            }

            mediaPlayer.prepareAsync()

        } catch (exception: Exception) {
            onError()
        }
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun seekTo(position: Int) {
        mediaPlayer.seekTo(position)
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun release() {
        mediaPlayer.release()
    }

}