package com.practicum.playlistmaker.presentation

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class MediaViewModel(
    private val track: Track
) : ViewModel() {

    private val mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())

    private var playerState = PlayerState.DEFAULT
    private var isPlayerInitialized = false
    private var currentProgress = 0L

    private val screenStateMutable = MutableLiveData<MediaScreenState>()
    val screenState: LiveData<MediaScreenState> = screenStateMutable

    private val updateTimerRunnable = object : Runnable {
        override fun run() {
            if (playerState == PlayerState.PLAYING) {
                currentProgress = mediaPlayer.currentPosition.toLong()
                renderState(mediaPlayer.currentPosition.toLong())
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
            mediaPlayer.pause()
            playerState = PlayerState.PAUSED
            handler.removeCallbacks(updateTimerRunnable)
            currentProgress = mediaPlayer.currentPosition.toLong()
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

        try {
            mediaPlayer.setDataSource(previewUrl)

            mediaPlayer.setOnPreparedListener {
                playerState = PlayerState.PREPARED
                currentProgress = 0L

                renderState(
                    currentProgress,
                    true
                )
            }

            mediaPlayer.setOnCompletionListener {
                playerState = PlayerState.PREPARED
                handler.removeCallbacks(updateTimerRunnable)

                mediaPlayer.seekTo(0)
                currentProgress = 0L

                renderState(progress = currentProgress)
            }

            mediaPlayer.prepareAsync()
        } catch (exception: Exception) {
            playerState = PlayerState.DEFAULT

            renderState(
                0L,
                false
            )
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.PLAYING

        handler.removeCallbacks(updateTimerRunnable)

        currentProgress = mediaPlayer.currentPosition.toLong()
        renderState(currentProgress)

        handler.post(updateTimerRunnable)
    }

    private fun renderState(
        progress: Long,
        isPlayButtonEnabled: Boolean = playerState != PlayerState.DEFAULT &&
                track.previewUrl?.isNotBlank() == true
    ) {
        screenStateMutable.value = MediaScreenState(
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
        mediaPlayer.release()
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