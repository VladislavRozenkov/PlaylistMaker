package com.practicum.playlistmaker.di

import androidx.room.Room
import com.practicum.playlistmaker.media_library.favorites.data.db.AppDatabase
import com.practicum.playlistmaker.media_library.favorites.data.mapper.FavoriteTrackMapper
import com.practicum.playlistmaker.media_library.favorites.data.repository.FavoriteTracksRepositoryImpl
import com.practicum.playlistmaker.media_library.favorites.domain.interactor.FavoriteTracksInteractor
import com.practicum.playlistmaker.media_library.favorites.domain.interactor.FavoriteTracksInteractorImpl
import com.practicum.playlistmaker.media_library.favorites.domain.repository.FavoriteTracksRepository
import com.practicum.playlistmaker.search.data.mapper.TrackMapper
import com.practicum.playlistmaker.search.data.network.ItunesApi
import com.practicum.playlistmaker.player.data.repository.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.settings.data.repository.SettingsRepositoryImpl
import com.practicum.playlistmaker.search.data.repository.TracksRepositoryImpl
import com.practicum.playlistmaker.search.data.storage.SearchHistoryStorage
import com.practicum.playlistmaker.settings.data.storage.SettingsStorage
import com.practicum.playlistmaker.player.domain.repository.AudioPlayerRepository
import com.practicum.playlistmaker.search.domain.repository.SearchHistoryRepository
import com.practicum.playlistmaker.settings.domain.repository.SettingsRepository
import com.practicum.playlistmaker.search.domain.repository.TracksRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://itunes.apple.com/"

val dataModule = module {

    factory<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(
            get()
        )
    }

    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(
            get(),
            get()
        )
    }

    single {
        FavoriteTrackMapper()
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "playlist_maker.db"
        ).build()
    }

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

    factory<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl()
    }
}