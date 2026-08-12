package com.practicum.playlistmaker.presentation

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.interactor.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class MediaViewModel(
    private val track: Track,
    private val audioPlayerInteractor: AudioPlayerInteractor
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private var playerState = PlayerState.DEFAULT
    private var isPlayerInitialized = false
    private var currentProgress = 0L

    private val _screenState = MutableLiveData<MediaScreenState>()
    val screenState: LiveData<MediaScreenState> = _screenState

    private val updateTimerRunnable = object : Runnable {
        override fun run() {
            if (playerState == PlayerState.PLAYING) {
                currentProgress = audioPlayerInteractor.getCurrentPosition().toLong()
                renderState(audioPlayerInteractor.getCurrentPosition().toLong())
                handler.postDelayed(this, TIMER_UPDATE_DELAY)
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
            handler.removeCallbacks(updateTimerRunnable)
            currentProgress = audioPlayerInteractor.getCurrentPosition().toLong()
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

                handler.removeCallbacks(
                    updateTimerRunnable
                )

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

        handler.removeCallbacks(updateTimerRunnable)

        currentProgress = audioPlayerInteractor.getCurrentPosition().toLong()
        renderState(currentProgress)

        handler.post(updateTimerRunnable)
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
            isPlayButtonEnabled
        )
    }

    private fun getCoverArtwork(artworkUrl100: String): String {
        return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    }

    private fun formatTime(millis: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
    }

    override fun onCleared() {
        super.onCleared()

        handler.removeCallbacks(updateTimerRunnable)
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
