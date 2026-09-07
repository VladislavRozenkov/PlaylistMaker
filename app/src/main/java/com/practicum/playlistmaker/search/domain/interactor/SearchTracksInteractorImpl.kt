package com.practicum.playlistmaker.search.domain.interactor

import com.practicum.playlistmaker.core.domain.model.Track
import com.practicum.playlistmaker.search.domain.repository.TracksRepository
import kotlinx.coroutines.flow.Flow

class SearchTracksInteractorImpl(
    private val repository: TracksRepository
) : SearchTracksInteractor {

    override fun execute(query: String): Flow<List<Track>> {
        return repository.searchTracks(query)
    }

}