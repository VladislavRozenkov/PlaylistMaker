package com.practicum.playlistmaker.media_library.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_library.playlists.domain.interactor.PlaylistInteractor
import com.practicum.playlistmaker.media_library.playlists.domain.model.Playlist
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _playlists = MutableLiveData<List<Playlist>>()

    val playlists: LiveData<List<Playlist>> = _playlists

    init {
        observePlaylists()
    }

    private fun observePlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { list ->
                _playlists.value = list
            }
        }
    }
}