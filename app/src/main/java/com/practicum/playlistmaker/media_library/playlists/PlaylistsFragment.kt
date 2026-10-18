package com.practicum.playlistmaker.media_library.playlists

import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding

class PlaylistsFragment : Fragment(R.layout.fragment_playlists) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentPlaylistsBinding.bind(view)

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_createPlaylistFragment
            )
        }
    }

    private val viewModel: PlaylistsViewModel by viewModel()

    companion object {
        fun newInstance(): PlaylistsFragment {
            return PlaylistsFragment()
        }
    }
}