package com.practicum.playlistmaker.media_library.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_library.favorites.domain.interactor.FavoriteTracksInteractor
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {

    private val _screenState =
        MutableLiveData<FavoriteTracksScreenState>()

    val screenState: LiveData<FavoriteTracksScreenState> =
        _screenState

    init {
        loadFavoriteTracks()
    }

    private fun loadFavoriteTracks() {
        viewModelScope.launch {

            favoriteTracksInteractor
                .getFavoriteTracks()
                .collect { tracks ->
                    if (tracks.isEmpty()) {
                        _screenState.value =
                            FavoriteTracksScreenState.Empty
                    } else {
                        _screenState.value =
                            FavoriteTracksScreenState.Content(tracks)
                    }
                }
        }
    }
}