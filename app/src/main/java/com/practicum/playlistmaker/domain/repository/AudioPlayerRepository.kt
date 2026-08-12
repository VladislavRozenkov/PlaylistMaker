package com.practicum.playlistmaker.domain.repository

interface AudioPlayerRepository {

    fun prepare(
        url: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit,
        onError: () -> Unit
    )

    fun start()

    fun pause()

    fun seekTo(position: Int)

    fun getCurrentPosition(): Int

    fun release()

}