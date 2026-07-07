package com.practicum.playlistmaker.presentation

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.databinding.ActivityMediaBinding
import com.practicum.playlistmaker.presentation.mapper.TrackParcelableMapper
import com.practicum.playlistmaker.presentation.models.TrackParcelable
import java.text.SimpleDateFormat
import java.util.Locale
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaBinding
    private lateinit var currentTrack: Track
    private val viewModel: MediaViewModel by viewModel {
        parametersOf(currentTrack)
    }
    private var currentArtworkUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = getTrackFromIntent()

        if (track == null) {
            finish()
            return
        }

        currentTrack = track

        setupInsets()
        setupListeners()
        observeViewModel()

        viewModel.onScreenOpened()
    }

    private fun getTrackFromIntent(): Track? {
        val trackParcelable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_TRACK, TrackParcelable::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_TRACK)
        }

        return trackParcelable?.let {
            TrackParcelableMapper.mapToTrack(it)
        }
    }

    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.play.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }
    }

    private fun observeViewModel() {
        viewModel.screenState.observe(this) { state ->
            render(state)
        }
    }

    private fun render(state: MediaScreenState) {
        binding.trackName.text = state.trackName
        binding.executor.text = state.artistName
        binding.time.text = state.progress
        binding.durationValue.text = state.duration
        binding.genreValue.text = state.genre
        binding.countryValue.text = state.country

        binding.play.isEnabled = state.isPlayButtonEnabled

        if (state.isPlaying) {
            binding.play.setImageResource(R.drawable.button_play_pause)
        } else {
            binding.play.setImageResource(R.drawable.button_play)
        }

        setOptionalField(
            binding.albumTitle,
            binding.albumValue,
            state.albumName
        )

        setOptionalField(
            binding.yearTitle,
            binding.yearValue,
            state.year
        )

        if (currentArtworkUrl != state.artworkUrl) {
            currentArtworkUrl = state.artworkUrl

            Glide.with(this)
                .load(state.artworkUrl)
                .placeholder(R.drawable.snake)
                .error(R.drawable.snake)
                .transform(
                    CenterCrop(),
                    RoundedCorners(resources.getDimensionPixelSize(R.dimen.cover_corner_radius))
                )
                .into(binding.imageCover)
        }
    }

    private fun setOptionalField(
        titleView: TextView,
        valueView: TextView,
        value: String
    ) {
        if (value.isEmpty()) {
            titleView.visibility = View.GONE
            valueView.visibility = View.GONE
        } else {
            titleView.visibility = View.VISIBLE
            valueView.visibility = View.VISIBLE
            valueView.text = value
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    companion object {
        private const val EXTRA_TRACK = "track"

        fun createIntent(context: Context, track: Track): Intent {
            return Intent(context, MediaActivity::class.java).apply {
                putExtra(EXTRA_TRACK, TrackParcelableMapper.mapToParcelable(track))
            }
        }
    }
}