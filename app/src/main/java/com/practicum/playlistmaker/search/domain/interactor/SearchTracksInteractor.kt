package com.practicum.playlistmaker.search.domain.interactor

import com.practicum.playlistmaker.core.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchTracksInteractor {
    fun execute(query: String): Flow<List<Track>>
}