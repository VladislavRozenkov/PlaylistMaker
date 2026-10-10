package com.practicum.playlistmaker.media_library.favorites.data.repository

import com.practicum.playlistmaker.core.domain.model.Track
import com.practicum.playlistmaker.media_library.favorites.data.db.AppDatabase
import com.practicum.playlistmaker.media_library.favorites.data.mapper.FavoriteTrackMapper
import com.practicum.playlistmaker.media_library.favorites.domain.repository.FavoriteTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteTracksRepositoryImpl(
    private val database: AppDatabase,
    private val mapper: FavoriteTrackMapper
) : FavoriteTracksRepository {

    override suspend fun addTrack(track: Track) {
        database.trackDao().insertTrack(
            mapper.mapTrackToEntity(track)
        )
    }

    override suspend fun deleteTrack(track: Track) {
        database.trackDao().deleteTrack(
            mapper.mapTrackToEntity(track)
        )
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return database
            .trackDao()
            .getFavoriteTracks()
            .map { entities  ->
                entities.map { entity ->
                    mapper.mapEntityToTrack(entity)
                }
            }

    }

    override fun observeIsFavorite(trackId: Long): Flow<Boolean> {
        return database.trackDao().observeIsFavorite(trackId)
    }
}