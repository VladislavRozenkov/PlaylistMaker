package com.practicum.playlistmaker.player.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.core.domain.model.Track
import com.practicum.playlistmaker.core.presentation.Event
import com.practicum.playlistmaker.media_library.favorites.domain.interactor.FavoriteTracksInteractor
import com.practicum.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.flow.catch

class MediaViewModel(
    private val track: Track,
    private val audioPlayerInteractor: AudioPlayerInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {

    private var timerJob: Job? = null
    private var playerState = PlayerState.DEFAULT
    private var isPlayerInitialized = false
    private var currentProgress = 0L
    private var isFavoriteLoaded = false
    private var pendingFavoriteState: Boolean? = null

    private val _screenState = MutableLiveData<MediaScreenState>()
    val screenState: LiveData<MediaScreenState> = _screenState

    private val _favoriteError = MutableLiveData<Event<Unit>>()
    val favoriteError: LiveData<Event<Unit>> = _favoriteError

    init {
        observeIsFavoriteState()
    }

    private fun startTimer() {
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            while (playerState == PlayerState.PLAYING) {
                currentProgress = audioPlayerInteractor
                    .getCurrentPosition()
                    .toLong()

                renderState(currentProgress)

                delay(TIMER_UPDATE_DELAY)
            }
        }
    }

    fun onScreenOpened() {
        renderState(
            currentProgress,
            playerState != PlayerState.DEFAULT
        )

        if (!isPlayerInitialized) {
            isPlayerInitialized = true
            preparePlayer()
        }
    }

    fun onPlayButtonClicked() {
        when (playerState) {
            PlayerState.PLAYING -> pausePlayer()
            PlayerState.PREPARED,
            PlayerState.PAUSED -> startPlayer()
            PlayerState.DEFAULT -> {}
        }
    }

    fun pausePlayer() {
        if (playerState == PlayerState.PLAYING) {
            audioPlayerInteractor.pause()
            playerState = PlayerState.PAUSED
            timerJob?.cancel()
            currentProgress = audioPlayerInteractor
                .getCurrentPosition()
                .toLong()
            renderState(currentProgress)
        }
    }

    private fun preparePlayer() {
        val previewUrl = track.previewUrl

        if (previewUrl.isNullOrBlank()) {
            renderState(
                0L,
                false
            )
            return
        }

        audioPlayerInteractor.prepare(
            url = previewUrl,

            onPrepared = {
                playerState = PlayerState.PREPARED
                currentProgress = 0L

                renderState(
                    currentProgress,
                    true
                )
            },

            onCompletion = {
                playerState = PlayerState.PREPARED

                timerJob?.cancel()

                audioPlayerInteractor.seekTo(0)

                currentProgress = 0L
                renderState(currentProgress)
            },

            onError = {
                playerState = PlayerState.DEFAULT

                renderState(
                    0L,
                    false
                )
            }
        )
    }

    private fun startPlayer() {
        audioPlayerInteractor.start()
        playerState = PlayerState.PLAYING

        startTimer()
    }

    fun onFavoriteClicked() {
        if (!isFavoriteLoaded || pendingFavoriteState != null) {
            return
        }

        val newFavoriteState = !track.isFavorite

        pendingFavoriteState = newFavoriteState
        renderState(currentProgress)

        viewModelScope.launch {
            try {
                if (newFavoriteState) {
                    favoriteTracksInteractor.addTrack(track)
                } else {
                    favoriteTracksInteractor.deleteTrack(track)
                }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                pendingFavoriteState = null
                renderState(currentProgress)
                _favoriteError.value = Event(Unit)
            }
        }
    }

    private fun renderState(
        progress: Long,
        isPlayButtonEnabled: Boolean = playerState != PlayerState.DEFAULT &&
                track.previewUrl?.isNotBlank() == true
    ) {
        _screenState.value = MediaScreenState(
            track.trackName,
            track.artistName,
            getCoverArtwork(track.artworkUrl100),
            formatTime(track.trackTimeMillis),
            track.collectionName.orEmpty(),
            track.releaseDate?.take(4).orEmpty(),
            track.primaryGenreName.orEmpty(),
            track.country.orEmpty(),
            formatTime(progress),
            playerState == PlayerState.PLAYING,
            isPlayButtonEnabled,
            track.isFavorite,
            isFavoriteLoaded && pendingFavoriteState == null
        )
    }

    private fun getCoverArtwork(artworkUrl100: String): String {
        return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    }

    private fun formatTime(millis: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
    }

    private fun observeIsFavoriteState() {
        viewModelScope.launch {
            favoriteTracksInteractor
                .observeIsFavorite(track.trackId)
                .catch { error ->
                    if (error is CancellationException) throw error
                    isFavoriteLoaded = false
                    pendingFavoriteState = null
                    renderState(currentProgress)
                    _favoriteError.value = Event(Unit)
                }
                .collect { isFavorite ->

                    track.isFavorite = isFavorite
                    isFavoriteLoaded = true

                    if (pendingFavoriteState == isFavorite) {
                        pendingFavoriteState = null
                    }

                    renderState(currentProgress)
                }
        }
    }

    override fun onCleared() {
        super.onCleared()

        audioPlayerInteractor.release()
    }

    private enum class PlayerState {
        DEFAULT,
        PREPARED,
        PLAYING,
        PAUSED
    }

    companion object {
        private const val TIMER_UPDATE_DELAY = 300L
    }

}
