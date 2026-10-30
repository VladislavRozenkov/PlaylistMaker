package com.practicum.playlistmaker.media_library.playlists.domain.repository

import com.practicum.playlistmaker.media_library.playlists.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun createPlaylist(
        playlist: Playlist,
        coverUri: String?
    ): Long

    suspend fun updatePlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>
}