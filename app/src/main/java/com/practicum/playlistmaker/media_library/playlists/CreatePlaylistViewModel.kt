package com.practicum.playlistmaker.media_library.playlists

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.core.presentation.Event
import com.practicum.playlistmaker.media_library.playlists.domain.interactor.PlaylistInteractor
import com.practicum.playlistmaker.media_library.playlists.domain.model.Playlist
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private var currentName = ""
    private var isSaving = false

    private val _isCreateEnabled = MutableLiveData(false)
    val isCreateEnabled: LiveData<Boolean> = _isCreateEnabled

    private val _coverUri = MutableLiveData<Uri?>(null)
    val coverUri: LiveData<Uri?> = _coverUri

    private val _saveResult =
        MutableLiveData<Event<CreatePlaylistSaveResult>>()

    val saveResult: LiveData<Event<CreatePlaylistSaveResult>> =
        _saveResult

    fun onNameChanged(name: String) {
        currentName = name
        updateCreateButton()
    }

    fun onCoverSelected(uri: Uri) {
        _coverUri.value = uri
    }

    fun createPlaylist(name: String, description: String) {
        val playlistName = name.trim()

        if (playlistName.isBlank() || isSaving) {
            return
        }

        isSaving = true
        updateCreateButton()

        viewModelScope.launch {
            try {
                val playlist = Playlist(
                    name = playlistName,
                    description = description.trim()
                )

                playlistInteractor.createPlaylist(
                    playlist = playlist,
                    coverUri = coverUri.value?.toString()
                )

                _saveResult.value = Event(
                    CreatePlaylistSaveResult.Success(playlistName)
                )
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _saveResult.value = Event(
                    CreatePlaylistSaveResult.Error
                )
            } finally {
                isSaving = false
                updateCreateButton()
            }
        }
    }

    private fun updateCreateButton() {
        _isCreateEnabled.value =
            currentName.isNotBlank() && !isSaving
    }
}