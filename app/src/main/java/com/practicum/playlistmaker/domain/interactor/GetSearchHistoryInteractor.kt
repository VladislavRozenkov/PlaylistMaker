package com.practicum.playlistmaker.domain.interactor

import com.practicum.playlistmaker.domain.models.Track

interface GetSearchHistoryInteractor {
    fun execute(): List<Track>
}