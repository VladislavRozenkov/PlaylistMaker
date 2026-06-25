package com.practicum.playlistmaker.domain.interactor

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.SearchHistoryRepository

class AddTrackToHistoryInteractorImpl(
    private val repository: SearchHistoryRepository
) : AddTrackToHistoryInteractor {

    override fun execute(track: Track) {
        val history = repository.getHistory().toMutableList()

        history.removeAll { savedTrack ->
            savedTrack.trackId == track.trackId
        }

        history.add(0, track)

        repository.saveHistory(history.take(MAX_HISTORY_SIZE))
    }

    companion object {
        private const val MAX_HISTORY_SIZE = 10
    }

}