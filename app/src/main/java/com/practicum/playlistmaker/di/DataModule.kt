package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.data.mapper.TrackMapper
import com.practicum.playlistmaker.data.network.ItunesApi
import com.practicum.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.data.repository.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.repository.TracksRepositoryImpl
import com.practicum.playlistmaker.data.storage.SearchHistoryStorage
import com.practicum.playlistmaker.data.storage.SettingsStorage
import com.practicum.playlistmaker.domain.repository.SearchHistoryRepository
import com.practicum.playlistmaker.domain.repository.SettingsRepository
import com.practicum.playlistmaker.domain.repository.TracksRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://itunes.apple.com/"

val dataModule = module {

    single {
        TrackMapper()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<ItunesApi> {
        get<Retrofit>().create(ItunesApi::class.java)
    }

    single {
        SearchHistoryStorage(androidContext())
    }

    single {
        SettingsStorage(androidContext())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(
            get(),
            get()
        )
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(
            get(),
            get()
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(
            get()
        )
    }
}