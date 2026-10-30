package com.practicum.playlistmaker.media_library.playlists.domain.interactor

import com.practicum.playlistmaker.media_library.playlists.domain.model.Playlist
import com.practicum.playlistmaker.media_library.playlists.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val repository: PlaylistRepository
) : PlaylistInteractor {

    override suspend fun createPlaylist(
        playlist: Playlist,
        coverUri: String?
    ): Long {
        return repository.createPlaylist(
            playlist,
            coverUri
        )
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        repository.updatePlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }
}