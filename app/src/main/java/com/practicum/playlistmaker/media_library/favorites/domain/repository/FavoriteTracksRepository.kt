package com.practicum.playlistmaker.media_library.favorites.domain.repository

import com.practicum.playlistmaker.core.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {

    suspend fun addTrack(track: Track)

    suspend fun deleteTrack(track: Track)

    fun getFavoriteTracks(): Flow<List<Track>>
}