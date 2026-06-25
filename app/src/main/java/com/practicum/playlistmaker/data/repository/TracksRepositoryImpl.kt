package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.dto.ItunesResponseDto
import com.practicum.playlistmaker.data.mapper.TrackMapper
import com.practicum.playlistmaker.data.network.ItunesApi
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.TracksRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TracksRepositoryImpl(
    private val api: ItunesApi,
    private val mapper: TrackMapper
) : TracksRepository {

    override fun searchTracks(
        query: String,
        onSuccess: (List<Track>) -> Unit,
        onError: () -> Unit
    ) {
        api.search(query).enqueue(object  : Callback<ItunesResponseDto> {

            override fun onResponse(
                call: Call<ItunesResponseDto>,
                response: Response<ItunesResponseDto>
            ) {
                if (response.isSuccessful) {
                    val tracks = response.body()?.results.orEmpty()
                        .map { trackDto ->
                            mapper.map(trackDto)
                        }
                    onSuccess(tracks)
                } else {
                    onError()
                }
            }

            override fun onFailure(call : Call<ItunesResponseDto>, t: Throwable) {
                onError()
            }
        } )
    }
}