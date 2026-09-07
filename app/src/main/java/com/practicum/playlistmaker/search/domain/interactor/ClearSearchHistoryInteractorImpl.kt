package com.practicum.playlistmaker.search.domain.interactor

import com.practicum.playlistmaker.search.domain.repository.SearchHistoryRepository

class ClearSearchHistoryInteractorImpl(
    private val repository: SearchHistoryRepository
) : ClearSearchHistoryInteractor {
    override fun execute() {
        repository.clearHistory()
    }
}