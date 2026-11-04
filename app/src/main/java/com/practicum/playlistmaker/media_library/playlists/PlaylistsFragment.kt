package com.practicum.playlistmaker.media_library.playlists

import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import android.graphics.Rect
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PlaylistsFragment : Fragment(R.layout.fragment_playlists) {

    private val viewModel: PlaylistsViewModel by viewModel()

    private val playlistAdapter = PlaylistAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentPlaylistsBinding.bind(view)

        binding.playlistsRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = playlistAdapter

            addItemDecoration(object : RecyclerView.ItemDecoration() {

                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    val position = parent.getChildAdapterPosition(view)

                    if (position == RecyclerView.NO_POSITION) return

                    val density = resources.displayMetrics.density
                    val horizontalGap = (8 * density).toInt()
                    val verticalGap = (16 * density).toInt()

                    if (position % 2 == 0) {
                        outRect.right = horizontalGap / 2
                    } else {
                        outRect.left = horizontalGap - horizontalGap / 2
                    }

                    outRect.bottom = verticalGap
                }
            })
        }

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_createPlaylistFragment
            )
        }

        viewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            val hasPlaylists = playlists.isNotEmpty()

            binding.playlistsRecyclerView.isVisible = hasPlaylists
            binding.emptyImage.isVisible = !hasPlaylists
            binding.emptyMessage.isVisible = !hasPlaylists

            playlistAdapter.updatePlaylists(playlists)
        }
    }

    override fun onDestroyView() {
        view?.findViewById<RecyclerView>(
            R.id.playlistsRecyclerView
        )?.adapter = null

        super.onDestroyView()
    }

    companion object {
        fun newInstance(): PlaylistsFragment {
            return PlaylistsFragment()
        }
    }
}