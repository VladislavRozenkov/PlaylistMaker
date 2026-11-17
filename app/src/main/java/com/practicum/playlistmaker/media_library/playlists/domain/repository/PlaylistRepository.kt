package com.practicum.playlistmaker.media_library.playlists.domain.repository

import com.practicum.playlistmaker.media_library.playlists.domain.model.Playlist
import kotlinx.coroutines.flow.Flow
import com.practicum.playlistmaker.core.domain.model.Track

interface PlaylistRepository {

    suspend fun addTrackToPlaylist(
        track: Track,
        playlist: Playlist
    )

    suspend fun createPlaylist(
        playlist: Playlist,
        coverUri: String?
    ): Long

    suspend fun updatePlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>
}