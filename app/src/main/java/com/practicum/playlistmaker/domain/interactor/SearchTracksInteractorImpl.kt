package com.practicum.playlistmaker.domain.interactor

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.TracksRepository
import kotlinx.coroutines.flow.Flow

class SearchTracksInteractorImpl(
    private val repository: TracksRepository
) : SearchTracksInteractor {

    override fun execute(query: String): Flow<List<Track>> {
        return repository.searchTracks(query)
    }

}