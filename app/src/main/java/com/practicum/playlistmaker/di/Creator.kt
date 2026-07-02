package com.practicum.playlistmaker.di

import android.content.Context
import com.practicum.playlistmaker.data.mapper.TrackMapper
import com.practicum.playlistmaker.data.network.ItunesApi
import com.practicum.playlistmaker.data.network.ItunesApiClient
import com.practicum.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.data.repository.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.repository.TracksRepositoryImpl
import com.practicum.playlistmaker.data.storage.SearchHistoryStorage
import com.practicum.playlistmaker.data.storage.SettingsStorage
import com.practicum.playlistmaker.domain.interactor.AddTrackToHistoryInteractor
import com.practicum.playlistmaker.domain.interactor.AddTrackToHistoryInteractorImpl
import com.practicum.playlistmaker.domain.interactor.ClearSearchHistoryInteractor
import com.practicum.playlistmaker.domain.interactor.ClearSearchHistoryInteractorImpl
import com.practicum.playlistmaker.domain.interactor.GetSearchHistoryInteractor
import com.practicum.playlistmaker.domain.interactor.GetSearchHistoryInteractorImpl
import com.practicum.playlistmaker.domain.interactor.GetThemeSettingsInteractor
import com.practicum.playlistmaker.domain.interactor.GetThemeSettingsInteractorImpl
import com.practicum.playlistmaker.domain.interactor.SearchTracksInteractor
import com.practicum.playlistmaker.domain.interactor.SearchTracksInteractorImpl
import com.practicum.playlistmaker.domain.interactor.UpdateThemeSettingsInteractor
import com.practicum.playlistmaker.domain.interactor.UpdateThemeSettingsInteractorImpl
import com.practicum.playlistmaker.domain.repository.SearchHistoryRepository
import com.practicum.playlistmaker.domain.repository.SettingsRepository
import com.practicum.playlistmaker.domain.repository.TracksRepository
import com.practicum.playlistmaker.presentation.MainViewModelFactory
import com.practicum.playlistmaker.presentation.SearchViewModelFactory
import com.practicum.playlistmaker.presentation.SettingsViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {

    private val trackMapper = TrackMapper()

    private val tracksRepository: TracksRepository = TracksRepositoryImpl(
        ItunesApiClient.api,
        trackMapper
    )

    fun provideSearchTracksInteractor(): SearchTracksInteractor {
        return SearchTracksInteractorImpl(tracksRepository)
    }

    private fun provideSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(
            SearchHistoryStorage(context),
            trackMapper
        )
    }

    fun provideGetSearchHistoryInteractor(context: Context): GetSearchHistoryInteractor {
        return GetSearchHistoryInteractorImpl(
            provideSearchHistoryRepository(context)
        )
    }

    fun provideAddTrackToHistoryInteractor(context: Context): AddTrackToHistoryInteractor {
        return AddTrackToHistoryInteractorImpl(
            provideSearchHistoryRepository(context)
        )
    }

    fun provideClearSearchHistoryInteractor(context: Context): ClearSearchHistoryInteractor {
        return ClearSearchHistoryInteractorImpl(
            provideSearchHistoryRepository(context)
        )
    }

    private fun provideSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(
            SettingsStorage(context)
        )
    }

    fun provideGetThemeSettingsInteractor(context: Context): GetThemeSettingsInteractor {
        return GetThemeSettingsInteractorImpl(
            provideSettingsRepository(context)
        )
    }

    fun provideUpdateThemeSettingsInteractor(context: Context): UpdateThemeSettingsInteractor {
        return UpdateThemeSettingsInteractorImpl(
            provideSettingsRepository(context)
        )
    }

    fun provideSearchViewModelFactory(context: Context): SearchViewModelFactory {
        return SearchViewModelFactory(
            provideSearchTracksInteractor(),
            provideGetSearchHistoryInteractor(context),
            provideAddTrackToHistoryInteractor(context),
            provideClearSearchHistoryInteractor(context)
        )
    }

    fun provideSettingsViewModelFactory(context: Context): SettingsViewModelFactory {
        return SettingsViewModelFactory(
            provideGetThemeSettingsInteractor(context),
            provideUpdateThemeSettingsInteractor(context)
        )
    }

    fun provideMainViewModelFactory(context: Context): MainViewModelFactory {
        return MainViewModelFactory(
            provideGetSearchHistoryInteractor(context)
        )
    }

}