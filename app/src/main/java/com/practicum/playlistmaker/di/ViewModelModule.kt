package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.core.domain.model.Track
import com.practicum.playlistmaker.media_library.favorites.FavoriteTracksViewModel
import com.practicum.playlistmaker.player.presentation.MediaViewModel
import com.practicum.playlistmaker.media_library.playlists.PlaylistsViewModel
import com.practicum.playlistmaker.search.presentation.SearchViewModel
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

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
            track,
            get()
        )
    }

    viewModel {
        FavoriteTracksViewModel()
    }

    viewModel {
        PlaylistsViewModel()
    }
}