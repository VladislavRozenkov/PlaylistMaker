package com.practicum.playlistmaker.search.domain.repository

import com.practicum.playlistmaker.core.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(query: String): Flow<List<Track>>
}