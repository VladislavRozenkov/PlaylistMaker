package com.practicum.playlistmaker.media_library.playlists.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.media_library.playlists.data.db.PlaylistEntity
import com.practicum.playlistmaker.media_library.playlists.domain.model.Playlist

class PlaylistMapper {

    private val gson = Gson()

    private val trackIdsType =
        object : TypeToken<List<Long>>() {}.type

    fun toEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            coverPath = playlist.coverPath,
            trackIdsJson = gson.toJson(playlist.trackIds),
            trackCount = playlist.trackCount
        )
    }

    fun toDomain(entity: PlaylistEntity): Playlist {
        val trackIds: List<Long> =
            gson.fromJson(entity.trackIdsJson, trackIdsType)
                ?: emptyList()

        return Playlist(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            coverPath = entity.coverPath,
            trackIds = trackIds
        )
    }
}