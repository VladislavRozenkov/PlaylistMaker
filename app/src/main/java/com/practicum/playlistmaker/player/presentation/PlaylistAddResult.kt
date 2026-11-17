package com.practicum.playlistmaker.player.presentation

sealed interface PlaylistAddResult {

    data class Added(
        val playlistName: String
    ) : PlaylistAddResult

    data class AlreadyExists(
        val playlistName: String
    ) : PlaylistAddResult

    data object Error : PlaylistAddResult
}
