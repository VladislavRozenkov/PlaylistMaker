package com.practicum.playlistmaker.domain.interactor

import com.practicum.playlistmaker.domain.repository.AudioPlayerRepository

class AudioPlayerInteractorImpl(
    private val repository: AudioPlayerRepository
) : AudioPlayerInteractor {

    override fun prepare(
        url: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit,
        onError: () -> Unit
    ) {
        repository.prepare(
            url,
            onPrepared,
            onCompletion,
            onError
        )
    }

    override fun start() {
        repository.start()
    }

    override fun pause() {
        repository.pause()
    }

    override fun seekTo(position: Int) {
        repository.seekTo(position)
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
    }

    override fun release() {
        repository.release()
    }

}