package com.practicum.playlistmaker.media_library.playlists

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentCreatePlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment : Fragment(R.layout.fragment_create_playlist) {

    private val viewModel: CreatePlaylistViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCreatePlaylistBinding.bind(view)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.playlistNameEditText.doAfterTextChanged { text ->
            viewModel.onNameChanged(text.toString())
        }

        viewModel.isCreateEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding.createPlaylistButton.isEnabled = isEnabled
        }
    }
}