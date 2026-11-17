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
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import android.widget.FrameLayout
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import kotlin.math.roundToInt
import android.util.TypedValue
import androidx.core.content.res.ResourcesCompat

class CreatePlaylistFragment : Fragment(R.layout.fragment_create_playlist) {

    private val viewModel: CreatePlaylistViewModel by viewModel()

    private val pickCoverLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.onCoverSelected(uri)
        }
    }

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


        binding.playlistCoverContainer.setOnClickListener {
            pickCoverLauncher.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }

        viewModel.coverUri.observe(viewLifecycleOwner) { uri ->
            val hasCover = uri != null

            binding.playlistCoverPlaceholder.isVisible = !hasCover
            binding.playlistCoverImage.isVisible = hasCover

            if (uri != null) {
                Glide.with(this)
                    .load(uri)
                    .centerCrop()
                    .into(binding.playlistCoverImage)
            }
        }

        binding.createPlaylistButton.setOnClickListener {
            viewModel.createPlaylist(
                name = binding.playlistNameEditText.text.toString(),
                description = binding.playlistDescriptionEditText.text.toString()
            )
        }

        viewModel.saveResult.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is CreatePlaylistSaveResult.Success -> {
                        val activity = requireActivity()

                        val message = getString(
                            R.string.playlist_created,
                            result.name
                        )

                        findNavController().navigateUp()

                        val snackbar = Snackbar.make(
                            activity.findViewById(android.R.id.content),
                            message,
                            Snackbar.LENGTH_LONG
                        )
                            .setBackgroundTint(
                                ContextCompat.getColor(
                                    activity,
                                    R.color.snackbar_background
                                )
                            )
                            .setTextColor(
                                ContextCompat.getColor(
                                    activity,
                                    R.color.snackbar_text
                                )
                            )

                        val density = resources.displayMetrics.density

                        val height = (48 * density).roundToInt()
                        val margin = (16 * density).roundToInt()

                        snackbar.view.layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            height,
                            Gravity.BOTTOM
                        ).apply {
                            setMargins(margin, 0, margin, margin)
                        }

                        val textView = snackbar.view.findViewById<TextView>(
                            com.google.android.material.R.id.snackbar_text
                        )

                        textView.apply {
                            typeface = ResourcesCompat.getFont(
                                activity,
                                R.font.ys_display_regular
                            )

                            setTextSize(
                                TypedValue.COMPLEX_UNIT_SP,
                                14f
                            )

                            letterSpacing = 0f
                            gravity = Gravity.CENTER
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                        }

                        snackbar.show()
                    }

                    CreatePlaylistSaveResult.Error -> {
                        Toast.makeText(
                            requireContext(),
                            R.string.playlist_creation_error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun attemptExit(binding: FragmentCreatePlaylistBinding) {

        val hasUnsavedData =
            !binding.playlistNameEditText.text.isNullOrEmpty() ||
                    !binding.playlistDescriptionEditText.text.isNullOrEmpty() ||
                    viewModel.coverUri.value != null

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