package com.practicum.playlistmaker.domain.interactor

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.TracksRepository

class SearchTracksInteractorImpl(
    private val repository: TracksRepository
) : SearchTracksInteractor {

    override fun execute(
        query: String,
        onSuccess: (List<Track>) -> Unit,
        onError: () -> Unit
    ) {
        repository.searchTracks(query, onSuccess, onError)
    }

}