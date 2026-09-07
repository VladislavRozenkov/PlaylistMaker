package com.practicum.playlistmaker.search.domain.interactor

import com.practicum.playlistmaker.core.domain.model.Track

interface GetSearchHistoryInteractor {
    fun execute(): List<Track>
}