package com.practicum.playlistmaker.di

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
import org.koin.dsl.module

val domainModule = module {

    single<SearchTracksInteractor> {
        SearchTracksInteractorImpl(
            get()
        )
    }

    single<GetSearchHistoryInteractor> {
        GetSearchHistoryInteractorImpl(
            get()
        )
    }

    single<AddTrackToHistoryInteractor> {
        AddTrackToHistoryInteractorImpl(
            get()
        )
    }

    single<ClearSearchHistoryInteractor> {
        ClearSearchHistoryInteractorImpl(
            get()
        )
    }

    single<GetThemeSettingsInteractor> {
        GetThemeSettingsInteractorImpl(
            get()
        )
    }

    single<UpdateThemeSettingsInteractor> {
        UpdateThemeSettingsInteractorImpl(
            get()
        )
    }
}