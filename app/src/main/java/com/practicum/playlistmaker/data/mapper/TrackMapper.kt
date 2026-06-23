package com.practicum.playlistmaker.data.mapper

import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.data.dto.TrackStorageDto
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackMapper {

    fun mapTrackToStorageDto(track: Track): TrackStorageDto {
        return TrackStorageDto(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun mapStorageDtoToTrack(dto: TrackStorageDto): Track {
        return Track(
            dto.trackId,
            dto.trackName,
            dto.artistName,
            dto.trackTime,
            dto.artworkUrl100,
            dto.collectionName,
            dto.releaseDate,
            dto.primaryGenreName,
            dto.country,
            dto.previewUrl
        )
    }

    fun map(dto: TrackDto): Track {
        return Track(
            dto.trackId,
            dto.trackName,
            dto.artistName,
            formatTime(dto.trackTimeMillis),
            dto.artworkUrl100,
            dto.collectionName,
            dto.releaseDate,
            dto.primaryGenreName,
            dto.country,
            dto.previewUrl
        )
    }

    private fun formatTime(millis: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
    }

}