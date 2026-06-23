package com.practicum.playlistmaker.domain.interactor

import com.practicum.playlistmaker.domain.models.Track

interface AddTrackToHistoryInteractor {
    fun execute(track: Track)
}