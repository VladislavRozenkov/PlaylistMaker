package com.practicum.playlistmaker.domain.interactor

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.SearchHistoryRepository

class GetSearchHistoryInteractorImpl(
    private val repository: SearchHistoryRepository
) : GetSearchHistoryInteractor {
    override fun execute(): List<Track> {
        return repository.getHistory()
    }
}