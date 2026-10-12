package com.practicum.playlistmaker.media_library.favorites.data.repository

import com.practicum.playlistmaker.core.domain.model.Track
import com.practicum.playlistmaker.media_library.favorites.data.db.AppDatabase
import com.practicum.playlistmaker.media_library.favorites.data.db.TrackDao
import com.practicum.playlistmaker.media_library.favorites.data.mapper.FavoriteTrackMapper
import com.practicum.playlistmaker.media_library.favorites.domain.repository.FavoriteTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteTracksRepositoryImpl(
    private val dao: TrackDao,
    private val mapper: FavoriteTrackMapper
) : FavoriteTracksRepository {

    override suspend fun addTrack(track: Track) {
        dao.insertTrack(
            mapper.mapTrackToEntity(track)
        )
    }

    override suspend fun deleteTrack(track: Track) {
        dao.deleteTrack(mapper.mapTrackToEntity(track))
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return dao.getFavoriteTracks()
            .map { entities ->
                entities.map { entity ->
                    mapper.mapEntityToTrack(
                        entity = entity,
                        isFavorite = true
                    )
                }
            }
    }

    override fun observeIsFavorite(trackId: Long): Flow<Boolean> {
        return dao.observeIsFavorite(trackId)
    }
}