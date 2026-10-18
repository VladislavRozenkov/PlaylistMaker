package com.practicum.playlistmaker.media_library.favorites.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Insert
    suspend fun insertTrack(track: FavoriteTrackEntity)

    @Delete
    suspend fun deleteTrack(track: FavoriteTrackEntity)

    @Query("SELECT * FROM favorite_tracks ORDER BY addedAt DESC")
    fun getFavoriteTracks(): Flow<List<FavoriteTrackEntity>>

    @Query("SELECT trackId FROM favorite_tracks")
    suspend fun gerFavoriteTrackIds(): List<Long>

    @Query(
        "SELECT EXISTS (SELECT 1 FROM favorite_tracks WHERE trackId = :trackId)"
    )
    fun observeIsFavorite(trackId: Long): Flow<Boolean>
}