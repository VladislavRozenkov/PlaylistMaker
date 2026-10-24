package com.practicum.playlistmaker.media_library.playlists.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val description: String = "",
    val coverPath: String? = null,
    val trackIdsJson: String = "[]",
    val trackCount: Int = 0
)
