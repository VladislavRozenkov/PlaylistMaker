package com.practicum.playlistmaker.media_library.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.domain.model.Track
import com.practicum.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.practicum.playlistmaker.player.presentation.MediaFragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.search.presentation.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {

    private var _binding: FragmentFavoriteTracksBinding? = null

    private val binding: FragmentFavoriteTracksBinding
    get() = _binding!!

    private val viewModel: FavoriteTracksViewModel by viewModel()

    private lateinit var tracksAdapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        contrianer: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        _binding = FragmentFavoriteTracksBinding.inflate(
            inflater,
            contrianer,
            false
        )
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        tracksAdapter = TrackAdapter(ArrayList())

        binding.favoriteTracksRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        binding.favoriteTracksRecyclerView.adapter = tracksAdapter
    }

    private fun observeViewModel() {
        viewModel.screenState.observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun render(state: FavoriteTracksScreenState) {
        when (state) {
            FavoriteTracksScreenState.Empty -> {
                showEmpty()
            }

            is FavoriteTracksScreenState.Content -> {
                showContent(state.tracks)
            }
        }
    }

    private fun showEmpty() {
        binding.favoriteTracksRecyclerView.visibility = View.GONE
        binding.emptyImage.visibility = View.VISIBLE
        binding.emptyMessage.visibility = View.VISIBLE
    }

    private fun showContent(tracks: List<Track>) {
        tracksAdapter.updateTracks(tracks)

        binding.favoriteTracksRecyclerView.visibility = View.VISIBLE
        binding.emptyImage.visibility = View.GONE
        binding.emptyMessage.visibility = View.GONE
    }

    private fun setupListeners() {
        tracksAdapter.onClick = { track ->
            openPlayer(track)
        }
    }

    private fun openPlayer(track: Track) {
        requireParentFragment()
            .findNavController()
            .navigate(
                R.id.action_mediaLibraryFragment_to_mediaFragment,
                MediaFragment.createArgs(track)
            )
    }

    override fun onDestroyView() {
        binding.favoriteTracksRecyclerView.adapter = null
        _binding = null

        super.onDestroyView()
    }

    companion object {
        fun newInstance(): FavoriteTracksFragment {
            return FavoriteTracksFragment()
        }
    }
}