package com.practicum.playlistmaker.media_library.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CreatePlaylistViewModel : ViewModel() {

    private val _isCreateEnabled = MutableLiveData(false)

    val isCreateEnabled: LiveData<Boolean> = _isCreateEnabled

    fun onNameChanged(name: String) {
        _isCreateEnabled.value = name.isNotBlank()
    }
}