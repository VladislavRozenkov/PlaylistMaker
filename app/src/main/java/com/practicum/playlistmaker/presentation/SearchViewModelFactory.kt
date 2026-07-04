package com.practicum.playlistmaker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.domain.interactor.AddTrackToHistoryInteractor
import com.practicum.playlistmaker.domain.interactor.ClearSearchHistoryInteractor
import com.practicum.playlistmaker.domain.interactor.GetSearchHistoryInteractor
import com.practicum.playlistmaker.domain.interactor.SearchTracksInteractor

class SearchViewModelFactory(
    private val searchTracksInteractor: SearchTracksInteractor,
    private val getSearchHistoryInteractor: GetSearchHistoryInteractor,
    private val addTrackToHistoryInteractor: AddTrackToHistoryInteractor,
    private val clearSearchHistoryInteractor: ClearSearchHistoryInteractor
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(
                searchTracksInteractor,
                getSearchHistoryInteractor,
                addTrackToHistoryInteractor,
                clearSearchHistoryInteractor
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}