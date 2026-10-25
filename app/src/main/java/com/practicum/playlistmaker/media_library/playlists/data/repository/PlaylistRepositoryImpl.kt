package com.practicum.playlistmaker.media_library.playlists.data.repository

import com.practicum.playlistmaker.media_library.playlists.data.db.PlaylistDao
import com.practicum.playlistmaker.media_library.playlists.data.mapper.PlaylistMapper
import com.practicum.playlistmaker.media_library.playlists.domain.model.Playlist
import com.practicum.playlistmaker.media_library.playlists.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val dao: PlaylistDao,
    private val mapper: PlaylistMapper
) : PlaylistRepository {

    override suspend fun createPlaylist(playlist: Playlist): Long {
        return dao.insertPlaylist(
            mapper.toEntity(playlist)
        )
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        dao.updatePlaylist(
            mapper.toEntity(playlist)
        )
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return dao.getPlaylists().map { entities ->
            entities.map { entity ->
                mapper.toDomain(entity)
            }
        }
    }
}