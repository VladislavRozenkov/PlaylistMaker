package com.practicum.playlistmaker.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.interactor.GetSearchHistoryInteractor
import com.practicum.playlistmaker.domain.models.Track

class MainViewModel(
    private val getSearchHistoryInteractor: GetSearchHistoryInteractor
) : ViewModel() {

    private val _openLastTrack = MutableLiveData<Event<Track>>()
    val openLastTrack: LiveData<Event<Track>> = _openLastTrack

    private val _showNoLastTrackMessage = MutableLiveData<Event<Unit>>()
    val showNoLastTrackMessage: LiveData<Event<Unit>> = _showNoLastTrackMessage

    fun onMediaClicked() {
        val lastTrack = getSearchHistoryInteractor.execute().firstOrNull()

        if (lastTrack != null) {
            _openLastTrack.value = Event(lastTrack)
        } else {
            _showNoLastTrackMessage.value = Event(Unit)
        }
    }
}
