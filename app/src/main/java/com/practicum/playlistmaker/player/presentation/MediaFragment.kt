package com.practicum.playlistmaker.player.presentation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior

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

        setupBottomSheet()
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

        binding.like.setOnClickListener {
            viewModel.onFavoriteClicked()
        }
    }

    private fun observeViewModel() {
        viewModel.favoriteError.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(
                    requireContext(),
                    R.string.favorite_operation_error,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.screenState.observe(viewLifecycleOwner) { state ->
            render(state)
        }


        viewModel.playlistAddResult.observe(viewLifecycleOwner) { event ->

            event.getContentIfNotHandled()?.let { result ->

                when (result) {

                    is PlaylistAddResult.Added -> {

                        BottomSheetBehavior.from(
                            binding.playlistsBottomSheet
                        ).state = BottomSheetBehavior.STATE_HIDDEN

                        Toast.makeText(
                            requireContext(),
                            getString(
                                R.string.track_added_to_playlist,
                                result.playlistName
                            ),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is PlaylistAddResult.AlreadyExists -> {

                        Toast.makeText(
                            requireContext(),
                            getString(
                                R.string.track_already_in_playlist,
                                result.playlistName
                            ),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    PlaylistAddResult.Error -> {

                        Toast.makeText(
                            requireContext(),
                            R.string.track_playlist_add_error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    }

    private fun render(state: MediaScreenState) {
        binding.trackName.text = state.trackName
        binding.executor.text = state.artistName
        binding.time.text = state.progress
        binding.durationValue.text = state.duration
        binding.genreValue.text = state.genre
        binding.countryValue.text = state.country
        binding.like.isEnabled = state.isFavoriteButtonEnable

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

        if (state.isFavorite) {
            binding.like.setImageResource(
                R.drawable.button_like
            )
        } else {
            binding.like.setImageResource(
                R.drawable.button_not_like
            )
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


    private fun setupBottomSheet() {

        val playlistAdapter = BottomSheetPlaylistAdapter()

        playlistAdapter.onPlaylistClick = { playlist ->
            viewModel.onPlaylistClicked(playlist)
        }

        binding.playlistsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = playlistAdapter
        }

        viewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            playlistAdapter.updatePlaylists(playlists)
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(
            binding.playlistsBottomSheet
        ).apply {
            isHideable = true
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(
                    bottomSheet: View,
                    newState: Int
                ) {
                    binding.overlay.isVisible =
                        newState != BottomSheetBehavior.STATE_HIDDEN
                }

                override fun onSlide(
                    bottomSheet: View,
                    slideOffset: Float
                ) {

                }
            }
        )

        binding.added.setOnClickListener {
            binding.overlay.isVisible = true
            bottomSheetBehavior.state =
                BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.overlay.setOnClickListener {
            bottomSheetBehavior.state =
                BottomSheetBehavior.STATE_HIDDEN
        }

        binding.newPlaylistButton.setOnClickListener {

            bottomSheetBehavior.state =
                BottomSheetBehavior.STATE_HIDDEN

            findNavController().navigate(
                R.id.action_mediaFragment_to_createPlaylistFragment
            )
        }
    }


    override fun onPause() {
        super.onPause()

        viewModel.pausePlayer()
    }

    override fun onDestroyView() {
        currentArtworkUrl = null

        binding.playlistsRecyclerView.adapter = null

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
