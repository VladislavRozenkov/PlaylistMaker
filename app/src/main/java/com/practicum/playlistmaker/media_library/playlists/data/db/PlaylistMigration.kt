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