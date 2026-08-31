package com.practicum.playlistmaker.domain.repository

import android.R
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

interface TracksRepository {
    fun searchTracks(query: String): Flow<List<Track>>
}