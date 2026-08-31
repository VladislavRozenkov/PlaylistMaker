package com.practicum.playlistmaker.presentation

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.interactor.AddTrackToHistoryInteractor
import com.practicum.playlistmaker.domain.interactor.ClearSearchHistoryInteractor
import com.practicum.playlistmaker.domain.interactor.GetSearchHistoryInteractor
import com.practicum.playlistmaker.domain.interactor.SearchTracksInteractor
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchTracksInteractor: SearchTracksInteractor,
    private val getSearchHistoryInteractor: GetSearchHistoryInteractor,
    private val addTrackToHistoryInteractor: AddTrackToHistoryInteractor,
    private val clearSearchHistoryInteractor: ClearSearchHistoryInteractor
) : ViewModel() {

    private var searchJob: Job? = null
    private var lastSearchQuery = ""
    private var currentText = ""
    private var isClickAllowed = true
    private var isScreenInitialized = false

    private val _screenState = MutableLiveData<SearchScreenState>()
    val screenState: LiveData<SearchScreenState> = _screenState
    private val _navEvents = MutableLiveData<Event<Track>>()
    val navEvents: LiveData<Event<Track>> = _navEvents

    fun onScreenOpened() {
        if (!isScreenInitialized) {
            isScreenInitialized = true
            showHistoryOrEmptyInput()
        }
    }

    fun onSearchTextChanged(text: String, hasFocus: Boolean) {
        currentText = text
        val query = text.trim()

        if (query.isEmpty()) {
            searchJob?.cancel()
            lastSearchQuery = ""
            showHistoryOrEmptyInput()
        } else {
            if (hasFocus) {
                _screenState.value = SearchScreenState.EmptyInput
            }

            searchDebounce(query)
        }
    }

    fun onSearchFocusChanged(hasFocus: Boolean) {
        if (hasFocus && currentText.isEmpty()) {
            showHistoryOrEmptyInput()
        } else if (currentText.isEmpty()) {
            _screenState.value = SearchScreenState.EmptyInput
        }
    }

    fun searchNow(query: String) {
        val trimmedQuery = query.trim()

        if (trimmedQuery.isNotEmpty()) {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                searchTracks(trimmedQuery)
            }
        }
    }

    fun retrySearch() {
        if (lastSearchQuery.isNotEmpty()) {
            searchJob = viewModelScope.launch {
                searchTracks(lastSearchQuery)
            }
        }
    }

    fun clearHistory() {
        clearSearchHistoryInteractor.execute()
        _screenState.value = SearchScreenState.EmptyInput
    }

    fun onTrackClicked(track: Track) {
        if (clickDebounce()) {
            addTrackToHistoryInteractor.execute(track)

            if (currentText.isEmpty()) {
                showHistoryOrEmptyInput()
            }
            _navEvents.value = Event(track)
        }
    }

    private fun searchDebounce(query: String) {

        lastSearchQuery = query

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchTracks(query)
        }
    }

    private suspend fun searchTracks(query: String) {
        lastSearchQuery = query
        _screenState.value = SearchScreenState.Loading

        searchTracksInteractor
            .execute(query)
            .catch {
                if (query == lastSearchQuery) {
                    _screenState.value = SearchScreenState.Error
                }
            }
            .collect { tracks ->
                if (query == lastSearchQuery) {
                    if (tracks.isEmpty()) {
                        _screenState.value =
                            SearchScreenState.EmptyResult
                    } else {
                        _screenState.value =
                            SearchScreenState.Content(tracks)
                    }
                }
            }
    }

    private fun showHistoryOrEmptyInput() {
        val history = getSearchHistoryInteractor.execute()

        if (history.isEmpty()) {
            _screenState.value = SearchScreenState.EmptyInput
        } else {
            _screenState.value = SearchScreenState.History(history)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed

        if (isClickAllowed) {
            isClickAllowed = false

            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }

        return current
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}
