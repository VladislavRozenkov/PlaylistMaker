package com.practicum.playlistmaker.media_library.playlists

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentCreatePlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment : Fragment(R.layout.fragment_create_playlist) {

    private val viewModel: CreatePlaylistViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCreatePlaylistBinding.bind(view)

        binding.playlistNameEditText.doAfterTextChanged { text ->
            viewModel.onNameChanged(text.toString())
        }

        viewModel.isCreateEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding.createPlaylistButton.isEnabled = isEnabled
        }

        binding.toolbar.setNavigationOnClickListener {
            attemptExit(binding)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            attemptExit(binding)
        }
    }

    private fun attemptExit(binding: FragmentCreatePlaylistBinding) {

        val hasUnsavedData =
            !binding.playlistNameEditText.text.isNullOrEmpty() ||
                    !binding.playlistDescriptionEditText.text.isNullOrEmpty()

        if (!hasUnsavedData) {
            findNavController().navigateUp()
            return
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.finish_playlist_creation)
            .setMessage(R.string.unsaved_playlist_data)
            .setNegativeButton(R.string.cancel_playlist_creation, null)
            .setPositiveButton(R.string.finish_playlist_creation_button) { _, _ ->
                findNavController().navigateUp()
            }
            .show()
    }
}