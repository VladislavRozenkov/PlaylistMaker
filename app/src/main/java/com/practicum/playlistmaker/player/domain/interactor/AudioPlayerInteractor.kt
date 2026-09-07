package com.practicum.playlistmaker.player.domain.interactor

interface AudioPlayerInteractor {

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