package com.practicum.playlistmaker.settings.presentation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.app.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    private val binding: FragmentSettingsBinding
        get() = _binding!!

    private val viewModel: SettingsViewModel by viewModel()

    private var isThemeSwitchChangingByCode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(
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
        binding.share.setOnClickListener {
            shareApp()
        }

        binding.support.setOnClickListener {
            sendEmailToSupport()
        }

        binding.agreement.setOnClickListener {
            openAgreement()
        }

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (!isThemeSwitchChangingByCode) {
                viewModel.onThemeSwitchChanged(isChecked)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.screenState.observe(viewLifecycleOwner) { state ->
            render(state)
        }

        viewModel.themeChanged.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { darkTheme ->
                val app = requireActivity().application as App
                app.switchTheme(darkTheme)
            }
        }
    }

    private fun render(state: SettingsScreenState) {
        isThemeSwitchChangingByCode = true
        binding.themeSwitch.isChecked = state.darkTheme
        isThemeSwitchChangingByCode = false
    }

    private fun shareApp() {
        val shareText = getString(R.string.shareText)

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        val chooserIntent = Intent.createChooser(
            shareIntent,
            getString(R.string.share_via)
        )

        try {
            startActivity(chooserIntent)
        } catch (exception: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                R.string.toastText,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun sendEmailToSupport() {
        val recipientEmail = getString(R.string.recipientEmail)
        val subject = getString(R.string.subject)
        val body = getString(R.string.body)

        val emailUri = Uri.parse("mailto:$recipientEmail")
            .buildUpon()
            .appendQueryParameter("subject", subject)
            .appendQueryParameter("body", body)
            .build()

        val emailIntent = Intent(
            Intent.ACTION_SEND,
            emailUri
        )

        val chooserIntent = Intent.createChooser(
            emailIntent,
            getString(R.string.email_chooser_title)
        )

        try {
            startActivity(chooserIntent)
        } catch (exception: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                R.string.toastText,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openAgreement() {
        val offerUrl = getString(R.string.offerUrl)

        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(offerUrl)
        )

        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}