package com.practicum.playlistmaker.media_library.favorites.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.media_library.playlists.data.db.PlaylistDao
import com.practicum.playlistmaker.media_library.playlists.data.db.PlaylistEntity

@Database(
    entities = [
        FavoriteTrackEntity::class,
        PlaylistEntity::class
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao

    abstract fun playlistDao(): PlaylistDao
}
