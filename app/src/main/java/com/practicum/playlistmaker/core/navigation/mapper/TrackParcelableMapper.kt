package com.practicum.playlistmaker.core.navigation.mapper

import com.practicum.playlistmaker.core.domain.model.Track
import com.practicum.playlistmaker.core.navigation.model.TrackParcelable

object TrackParcelableMapper {

    fun mapToParcelable(track: Track): TrackParcelable {
        return TrackParcelable(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
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
            trackParcelable.trackTimeMillis,
            trackParcelable.artworkUrl100,
            trackParcelable.collectionName,
            trackParcelable.releaseDate,
            trackParcelable.primaryGenreName,
            trackParcelable.country,
            trackParcelable.previewUrl
        )
    }

}