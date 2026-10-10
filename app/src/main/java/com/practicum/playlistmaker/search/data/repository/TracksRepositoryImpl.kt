package com.practicum.playlistmaker.search.data.repository

import com.practicum.playlistmaker.core.domain.model.Track
import com.practicum.playlistmaker.media_library.favorites.data.db.AppDatabase
import com.practicum.playlistmaker.search.domain.repository.TracksRepository
import com.practicum.playlistmaker.search.data.mapper.TrackMapper
import com.practicum.playlistmaker.search.data.network.ItunesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val api: ItunesApi,
    private val mapper: TrackMapper,
    private val database: AppDatabase
) : TracksRepository {

    override fun searchTracks(query: String): Flow<List<Track>> = flow {

        val response = api.search(query)

        val favoriteTrackIds = database
            .trackDao()
            .gerFavoriteTrackIds()
            .toSet()

        val tracks = response.results.map { trackDto ->

            mapper.map(trackDto).apply {
                isFavorite = trackId in favoriteTrackIds
            }
        }

        emit(tracks)
    }
}