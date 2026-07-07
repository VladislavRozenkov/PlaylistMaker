package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.MainViewModel
import com.practicum.playlistmaker.presentation.MediaViewModel
import com.practicum.playlistmaker.presentation.SearchViewModel
import com.practicum.playlistmaker.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MainViewModel(
            get()
        )
    }

    viewModel {
        SearchViewModel(
            get(),
            get(),
            get(),
            get()
        )
    }

    viewModel {
        SettingsViewModel(
            get(),
            get()
        )
    }

    viewModel { (track: Track) ->
        MediaViewModel(
            track
        )
    }
}