package com.practicum.playlistmaker.domain.interactor

import com.practicum.playlistmaker.domain.models.Track

interface SearchTracksInteractor {
    fun execute(
        query: String,
        onSuccess: (List<Track>) -> Unit,
        onError: () -> Unit
    )
}