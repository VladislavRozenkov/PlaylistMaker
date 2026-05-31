package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.databinding.ActivityMediaBinding
import java.text.SimpleDateFormat
import java.util.Locale
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper

class MediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaBinding

    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())
    private val updateTimerRunnable = object : Runnable {
        override fun run() {
            if (playerState == STATE_PLAYING) {
                binding.time.text = formatTime(mediaPlayer.currentPosition.toLong())
                handler.postDelayed(this, DELAY)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        renderTrack()
    }

    private fun renderTrack() {

        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_TRACK, Track::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_TRACK)
        } ?: return

        val previewUrl = track.previewUrl

        if (!previewUrl.isNullOrBlank()) {
            preparePlayer(previewUrl)
        }

        binding.play.setOnClickListener {
            playbackControl()
        }

        binding.trackName.text = track.trackName
        binding.executor.text = track.artistName
        binding.time.text = formatTime(0L)
        binding.durationValue.text = track.trackTime
        setOptionalField(
            binding.albumTitle,
            binding.albumValue,
            track.collectionName.orEmpty()
        )
        setOptionalField(
            binding.yearTitle,
            binding.yearValue,
            track.releaseDate?.take(4).orEmpty()
        )
        binding.genreValue.text = track.primaryGenreName.orEmpty()
        binding.countryValue.text = track.country.orEmpty()

        Glide.with(this)
            .load(getCoverArtwork(track.artworkUrl100))
            .placeholder(R.drawable.snake)
            .error(R.drawable.snake)
            .transform(CenterCrop(),
                RoundedCorners(resources.getDimensionPixelSize(R.dimen.cover_corner_radius)))
            .into(binding.imageCover)
    }

    private fun getCoverArtwork(artworkUrl100: String): String {
        return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    }

    private fun setOptionalField(titleView: TextView, valueView: TextView, value: String) {

        if (value.isEmpty()) {
            titleView.visibility = View.GONE
            valueView.visibility = View.GONE
        } else {
            titleView.visibility = View.VISIBLE
            valueView.visibility = View.VISIBLE
            valueView.text = value
        }
    }

    private fun formatTime(millis: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)

        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }

        mediaPlayer.setOnCompletionListener {
            binding.play.setImageResource(R.drawable.button_play)
            binding.time.text = formatTime(0L)
            playerState = STATE_PREPARED
            handler.removeCallbacks(updateTimerRunnable)
        }

        mediaPlayer.prepareAsync()
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSED -> startPlayer()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.play.setImageResource(R.drawable.button_play_pause)
        playerState = STATE_PLAYING
        handler.post(updateTimerRunnable)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.play.setImageResource(R.drawable.button_play)
        playerState = STATE_PAUSED
        handler.removeCallbacks(updateTimerRunnable)
    }

    override fun onPause() {
        super.onPause()
        if (playerState == STATE_PLAYING) {
            pausePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTimerRunnable)
        mediaPlayer.release()
    }

    companion object {
        private const val EXTRA_TRACK = "track"

        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val DELAY = 300L

        fun createIntent(context: Context, track: Track): Intent {
            return Intent(context, MediaActivity::class.java).apply {
                putExtra(EXTRA_TRACK, track)
            }
        }
    }
}