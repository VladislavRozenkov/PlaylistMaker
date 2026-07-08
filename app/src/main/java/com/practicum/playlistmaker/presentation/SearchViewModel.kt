package com.practicum.playlistmaker.presentation

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.interactor.AddTrackToHistoryInteractor
import com.practicum.playlistmaker.domain.interactor.ClearSearchHistoryInteractor
import com.practicum.playlistmaker.domain.interactor.GetSearchHistoryInteractor
import com.practicum.playlistmaker.domain.interactor.SearchTracksInteractor
import com.practicum.playlistmaker.domain.models.Track

class SearchViewModel(
    private val searchTracksInteractor: SearchTracksInteractor,
    private val getSearchHistoryInteractor: GetSearchHistoryInteractor,
    private val addTrackToHistoryInteractor: AddTrackToHistoryInteractor,
    private val clearSearchHistoryInteractor: ClearSearchHistoryInteractor
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())
    private var lastSearchQuery = ""
    private var currentText = ""
    private var isClickAllowed = true
    private var isScreenInitialized = false

    private val _screenState = MutableLiveData<SearchScreenState>()
    val screenState: LiveData<SearchScreenState> = _screenState

    private val _navEvents = MutableLiveData<Event<Track>>()
    val navEvents: LiveData<Event<Track>> = _navEvents

    private val searchRunnable = Runnable {
        if (lastSearchQuery.isNotEmpty()) {
            searchTracks(lastSearchQuery)
        }
    }

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
            handler.removeCallbacks(searchRunnable)
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
            handler.removeCallbacks(searchRunnable)
            searchTracks(trimmedQuery)
        }
    }

    fun retrySearch() {
        if (lastSearchQuery.isNotEmpty()) {
            searchTracks(lastSearchQuery)
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
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchTracks(query: String) {
        lastSearchQuery = query
        _screenState.value = SearchScreenState.Loading

        searchTracksInteractor.execute(
            query,
            { tracks ->
                if (tracks.isEmpty()) {
                    _screenState.postValue(SearchScreenState.EmptyResult)
                } else {
                    _screenState.postValue(SearchScreenState.Content(tracks))
                }
            },
            {
                _screenState.postValue(SearchScreenState.Error)
            }
        )
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
            handler.postDelayed(
                { isClickAllowed = true },
                CLICK_DEBOUNCE_DELAY
            )
        }

        return current
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(searchRunnable)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}
