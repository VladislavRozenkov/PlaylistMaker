package com.practicum.playlistmaker.search.domain.interactor

import com.practicum.playlistmaker.core.domain.model.Track

interface AddTrackToHistoryInteractor {
    fun execute(track: Track)
}