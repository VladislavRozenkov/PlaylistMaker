package com.practicum.playlistmaker.media_library.favorites.data.db

import androidx.room.Database
import com.practicum.playlistmaker.media_library.playlists.data.db.PlaylistTrackEntity
import com.practicum.playlistmaker.media_library.playlists.data.db.PlaylistTrackDao
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.media_library.playlists.data.db.PlaylistDao
import com.practicum.playlistmaker.media_library.playlists.data.db.PlaylistEntity

@Database(
    entities = [
        FavoriteTrackEntity::class,
        PlaylistEntity::class,
        PlaylistTrackEntity::class
    ],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun playlistTrackDao(): PlaylistTrackDao
}
