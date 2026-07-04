package com.practicum.playlistmaker.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.interactor.GetSearchHistoryInteractor
import com.practicum.playlistmaker.domain.models.Track

class MainViewModel(
    private val getSearchHistoryInteractor: GetSearchHistoryInteractor
) : ViewModel() {

    private val openLastTrackMutable = MutableLiveData<Event<Track>>()
    val openLastTrack: LiveData<Event<Track>> = openLastTrackMutable

    private val showNoLastTrackMessageMutable = MutableLiveData<Event<Unit>>()
    val showNoLastTrackMessage: LiveData<Event<Unit>> = showNoLastTrackMessageMutable

    fun onMediaClicked() {
        val lastTrack = getSearchHistoryInteractor.execute().firstOrNull()

        if (lastTrack != null) {
            openLastTrackMutable.value = Event(lastTrack)
        } else {
            showNoLastTrackMessageMutable.value = Event(Unit)
        }
    }
}