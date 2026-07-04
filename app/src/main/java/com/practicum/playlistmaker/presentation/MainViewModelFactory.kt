package com.practicum.playlistmaker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.domain.interactor.GetSearchHistoryInteractor

class MainViewModelFactory(
    private val getSearchHistoryInteractor: GetSearchHistoryInteractor
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(getSearchHistoryInteractor) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}