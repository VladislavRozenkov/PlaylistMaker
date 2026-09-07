package com.practicum.playlistmaker.search.domain.repository

import com.practicum.playlistmaker.core.domain.model.Track

interface SearchHistoryRepository {
    fun getHistory(): List<Track>
    fun saveHistory(history: List<Track>)
    fun clearHistory()
}