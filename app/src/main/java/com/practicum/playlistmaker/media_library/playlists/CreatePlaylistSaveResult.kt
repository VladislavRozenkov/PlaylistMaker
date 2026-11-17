package com.practicum.playlistmaker.media_library.playlists

sealed interface CreatePlaylistSaveResult {

    data class Success(
        val name: String
    ) : CreatePlaylistSaveResult

    data object Error : CreatePlaylistSaveResult
}