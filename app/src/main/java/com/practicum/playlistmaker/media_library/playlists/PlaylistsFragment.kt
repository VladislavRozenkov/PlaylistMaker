package com.practicum.playlistmaker.media_library.playlists

import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment(R.layout.fragment_playlists) {

    private val viewModel: PlaylistsViewModel by viewModel()

    companion object {
        fun newInstance(): PlaylistsFragment {
            return PlaylistsFragment()
        }
    }
}