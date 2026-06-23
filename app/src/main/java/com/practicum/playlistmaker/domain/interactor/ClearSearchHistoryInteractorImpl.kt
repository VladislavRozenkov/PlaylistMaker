package com.practicum.playlistmaker.domain.interactor

import com.practicum.playlistmaker.domain.repository.SearchHistoryRepository

class ClearSearchHistoryInteractorImpl(
    private val repository: SearchHistoryRepository
) : ClearSearchHistoryInteractor {
    override fun execute() {
        repository.clearHistory()
    }
}