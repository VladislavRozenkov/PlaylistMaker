package com.practicum.playlistmaker.player.presentation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMediaBinding
import com.practicum.playlistmaker.core.domain.model.Track
import com.practicum.playlistmaker.core.navigation.mapper.TrackParcelableMapper
import com.practicum.playlistmaker.core.navigation.model.TrackParcelable
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MediaFragment : Fragment() {

    private var _binding: FragmentMediaBinding? = null

    private val binding: FragmentMediaBinding
        get() = _binding!!

    private lateinit var currentTrack: Track

    private val viewModel: MediaViewModel by viewModel {
        parametersOf(currentTrack)
    }

    private var currentArtworkUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentTrack = getTrackFromArguments()!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        observeViewModel()

        viewModel.onScreenOpened()
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.play.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }
    }

    private fun observeViewModel() {
        viewModel.screenState.observe(viewLifecycleOwner) { state ->
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
            binding.play.setImageResource(
                R.drawable.button_play_pause
            )
        } else {
            binding.play.setImageResource(
                R.drawable.button_play
            )
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
                    RoundedCorners(
                        resources.getDimensionPixelSize(
                            R.dimen.cover_corner_radius
                        )
                    )
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

    private fun getTrackFromArguments(): Track? {
        val trackParcelable =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getParcelable(
                    ARG_TRACK,
                    TrackParcelable::class.java
                )
            } else {
                @Suppress("DEPRECATION")
                arguments?.getParcelable<TrackParcelable>(
                    ARG_TRACK
                )
            }

        return trackParcelable?.let {
            TrackParcelableMapper.mapToTrack(it)
        }
    }

    override fun onPause() {
        super.onPause()

        viewModel.pausePlayer()
    }

    override fun onDestroyView() {
        currentArtworkUrl = null

        _binding = null

        super.onDestroyView()
    }

    companion object {

        private const val ARG_TRACK = "track"

        fun createArgs(track: Track): Bundle {
            val trackParcelable =
                TrackParcelableMapper.mapToParcelable(track)

            return bundleOf(
                ARG_TRACK to trackParcelable
            )
        }
    }

}