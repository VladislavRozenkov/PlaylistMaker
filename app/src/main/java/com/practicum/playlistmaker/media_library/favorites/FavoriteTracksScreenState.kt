package com.practicum.playlistmaker.media_library.favorites

import com.practicum.playlistmaker.core.domain.model.Track

sealed interface FavoriteTracksScreenState {

    data object Empty : FavoriteTracksScreenState

    data class Content(
        val tracks: List<Track>
    ) : FavoriteTracksScreenState
}