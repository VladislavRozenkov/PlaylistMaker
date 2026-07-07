package com.practicum.playlistmaker.presentation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModel()
    private var isThemeSwitchChangingByCode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupInsets()
        setupListeners()
        observeViewModel()

        viewModel.onScreenOpened()
    }

    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

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
        viewModel.screenState.observe(this) { state ->
            render(state)
        }

        viewModel.themeChanged.observe(this) { event ->
            event.getContentIfNotHandled()?.let { darkTheme ->
                (applicationContext as App).switchTheme(darkTheme)
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

        val shareVia = getString(R.string.share_via)

        try {
            startActivity(Intent.createChooser(shareIntent, shareVia))
        } catch (exception: ActivityNotFoundException) {
            Toast.makeText(this, R.string.toastText, Toast.LENGTH_SHORT).show()
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

        val emailIntent = Intent(Intent.ACTION_SENDTO, emailUri)

        try {
            startActivity(
                Intent.createChooser(
                    emailIntent,
                    getString(R.string.email_chooser_title)
                )
            )
        } catch (exception: ActivityNotFoundException) {
            Toast.makeText(
                this,
                R.string.toastText,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openAgreement() {
        val offerUrl = getString(R.string.offerUrl)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(offerUrl))
        startActivity(intent)
    }

}
