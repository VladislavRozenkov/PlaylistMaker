package com.practicum.playlistmaker.search.data.repository

import com.practicum.playlistmaker.search.data.storage.SearchHistoryStorage
import com.practicum.playlistmaker.core.domain.model.Track
import com.practicum.playlistmaker.media_library.favorites.data.db.AppDatabase
import com.practicum.playlistmaker.search.domain.repository.SearchHistoryRepository
import com.practicum.playlistmaker.search.data.mapper.TrackMapper

class SearchHistoryRepositoryImpl(
    private val storage: SearchHistoryStorage,
    private val mapper: TrackMapper,
) : SearchHistoryRepository {

    override suspend fun getHistory(): List<Track> {
        return storage.getHistory().map { trackStorageDto ->
            mapper.mapStorageDtoToTrack(trackStorageDto)
        }
    }

    override fun saveHistory(history: List<Track>) {
        val historyDto = history.map { track ->
            mapper.mapTrackToStorageDto(track)
        }

        storage.saveHistory(historyDto)
    }

    override fun clearHistory() {
        storage.clearHistory()
    }
}