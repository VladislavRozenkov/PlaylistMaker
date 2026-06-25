package com.practicum.playlistmaker.presentation.mapper

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.models.TrackParcelable

object TrackParcelableMapper {

    fun mapToParcelable(track: Track): TrackParcelable {
        return TrackParcelable(
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

    fun mapToTrack(trackParcelable: TrackParcelable): Track {
        return Track(
            trackParcelable.trackId,
            trackParcelable.trackName,
            trackParcelable.artistName,
            trackParcelable.trackTime,
            trackParcelable.artworkUrl100,
            trackParcelable.collectionName,
            trackParcelable.releaseDate,
            trackParcelable.primaryGenreName,
            trackParcelable.country,
            trackParcelable.previewUrl
        )
    }

}