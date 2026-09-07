package com.practicum.playlistmaker.search.presentation

import com.practicum.playlistmaker.core.domain.model.Track

sealed interface SearchScreenState {

    data object EmptyInput : SearchScreenState

    data object Loading : SearchScreenState

    data class Content(
        val tracks: List<Track>
    ) : SearchScreenState

    data object EmptyResult : SearchScreenState

    data object Error : SearchScreenState

    data class History(
        val tracks: List<Track>
    ) : SearchScreenState
}