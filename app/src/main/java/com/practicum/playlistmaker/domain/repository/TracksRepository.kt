package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks(
        query: String,
        onSuccess: (List<Track>) -> Unit,
        onError: () -> Unit
    )
}