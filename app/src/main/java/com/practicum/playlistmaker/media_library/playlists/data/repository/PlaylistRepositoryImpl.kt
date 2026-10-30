package com.practicum.playlistmaker.media_library.playlists.data.repository

import com.practicum.playlistmaker.media_library.playlists.data.db.PlaylistDao
import com.practicum.playlistmaker.media_library.playlists.data.mapper.PlaylistMapper
import com.practicum.playlistmaker.media_library.playlists.data.storage.PlaylistCoverStorage
import com.practicum.playlistmaker.media_library.playlists.domain.model.Playlist
import com.practicum.playlistmaker.media_library.playlists.domain.repository.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File

class PlaylistRepositoryImpl(
    private val dao: PlaylistDao,
    private val mapper: PlaylistMapper,
    private val coverStorage: PlaylistCoverStorage
) : PlaylistRepository {

    override suspend fun createPlaylist(
        playlist: Playlist,
        coverUri: String?
    ): Long = withContext(Dispatchers.IO) {

        val savedCoverPath = coverUri?.let {
            coverStorage.saveCover(it)
        }

        try {
            val playlistWithCover = playlist.copy(
                coverPath = savedCoverPath
            )

            dao.insertPlaylist(
                mapper.toEntity(playlistWithCover)
            )
        } catch (error: Exception) {
            savedCoverPath?.let {
                File(it).delete()
            }
            throw error
        }
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