package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.dto.ItunesResponseDto
import com.practicum.playlistmaker.data.mapper.TrackMapper
import com.practicum.playlistmaker.data.network.ItunesApi
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TracksRepositoryImpl(
    private val api: ItunesApi,
    private val mapper: TrackMapper
) : TracksRepository {

    override fun searchTracks(query: String): Flow<List<Track>> = flow {

        val response = api.search(query)

        val tracks = response.results.map { trackDto ->
            mapper.map(trackDto)
        }

        emit(tracks)
    }
}