package com.practicum.playlistmaker.domain.interactor

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.SearchHistoryRepository

class AddTrackToHistoryInteractorImpl(
    private val repository: SearchHistoryRepository
) : AddTrackToHistoryInteractor {
    override fun execute(track: Track) {
        repository.addTrack(track)
    }
}