package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.data.mapper.TrackMapper
import com.practicum.playlistmaker.data.network.ItunesApi
import com.practicum.playlistmaker.data.repository.TracksRepositoryImpl
import com.practicum.playlistmaker.domain.interactor.SearchTracksInteractor
import com.practicum.playlistmaker.domain.interactor.SearchTracksInteractorImpl
import com.practicum.playlistmaker.domain.repository.TracksRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {

    private const val ITUNES_BASE_URL = "https://itunes.apple.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ItunesApi::class.java)

    private val trackMapper = TrackMapper()

    private val tracksRepository: TracksRepository = TracksRepositoryImpl(
        api,
        trackMapper
    )

    fun provideSearchTracksInteractor(): SearchTracksInteractor {
        return SearchTracksInteractorImpl(tracksRepository)
    }
}