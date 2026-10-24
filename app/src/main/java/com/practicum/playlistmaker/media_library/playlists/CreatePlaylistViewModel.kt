package com.practicum.playlistmaker.media_library.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.net.Uri

class CreatePlaylistViewModel : ViewModel() {

    private val _isCreateEnabled = MutableLiveData(false)

    val isCreateEnabled: LiveData<Boolean> = _isCreateEnabled

    fun onNameChanged(name: String) {
        _isCreateEnabled.value = name.isNotBlank()
    }


    private val _coverUri = MutableLiveData<Uri?>(null)

    val coverUri: LiveData<Uri?> = _coverUri

    fun onCoverSelected(uri: Uri) {
        _coverUri.value = uri
    }
}