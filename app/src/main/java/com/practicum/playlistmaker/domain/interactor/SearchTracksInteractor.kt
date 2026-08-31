package com.practicum.playlistmaker.domain.interactor

import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchTracksInteractor {
    fun execute(query: String): Flow<List<Track>>
}