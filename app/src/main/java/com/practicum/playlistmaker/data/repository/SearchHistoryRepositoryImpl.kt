package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.mapper.TrackMapper
import com.practicum.playlistmaker.data.storage.SearchHistoryStorage
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.SearchHistoryRepository

class SearchHistoryRepositoryImpl(
    private val storage: SearchHistoryStorage,
    private val mapper: TrackMapper
) : SearchHistoryRepository {

    override fun getHistory(): List<Track> {
        return storage.getHistory().map { trackStorageDto ->
            mapper.mapStorageDtoToTrack(trackStorageDto)
        }
    }

    override fun addTrack(track: Track) {
        storage.addTrack(mapper.mapTrackToStorageDto(track))
    }

    override fun clearHistory() {
        storage.clearHistory()
    }
}