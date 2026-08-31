package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.dto.ItunesResponseDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {
    @GET("search?entity=song")
    suspend fun search(
        @Query("term") text: String): ItunesResponseDto
}