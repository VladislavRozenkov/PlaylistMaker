package com.practicum.playlistmaker.search.domain.interactor

import com.practicum.playlistmaker.core.domain.model.Track
import com.practicum.playlistmaker.search.domain.repository.SearchHistoryRepository

class GetSearchHistoryInteractorImpl(
    private val repository: SearchHistoryRepository
) : GetSearchHistoryInteractor {
    override fun execute(): List<Track> {
        return repository.getHistory()
    }
}