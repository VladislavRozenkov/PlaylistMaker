package com.practicum.playlistmaker.media_library.playlists.data.db

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `playlists` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `name` TEXT NOT NULL,
                `description` TEXT NOT NULL,
                `coverPath` TEXT,
                `trackIdsJson` TEXT NOT NULL,
                `trackCount` INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {

    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `playlist_tracks` (
                `trackId` INTEGER NOT NULL PRIMARY KEY,
                `trackName` TEXT NOT NULL,
                `artistName` TEXT NOT NULL,
                `trackTimeMillis` INTEGER NOT NULL,
                `artworkUrl100` TEXT NOT NULL,
                `collectionName` TEXT,
                `releaseDate` TEXT,
                `primaryGenreName` TEXT,
                `country` TEXT,
                `previewUrl` TEXT,
                `addedAt` INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }
}