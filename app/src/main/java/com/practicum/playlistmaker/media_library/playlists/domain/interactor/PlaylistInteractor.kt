package com.practicum.playlistmaker.media_library.playlists.domain.interactor

import com.practicum.playlistmaker.media_library.playlists.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun createPlaylist(
        playlist: Playlist,
        coverUri: String?
    ): Long

    suspend fun updatePlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>
}