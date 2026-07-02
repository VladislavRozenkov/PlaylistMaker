package com.practicum.playlistmaker.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.di.Creator
import com.practicum.playlistmaker.domain.models.Track

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            Creator.provideMainViewModelFactory(applicationContext)
        )[MainViewModel::class.java]

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.search.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        binding.media.setOnClickListener {
            viewModel.onMediaClicked()
        }

        binding.settings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun observeViewModel() {
        viewModel.openLastTrack.observe(this) { event ->
            event.getContentIfNotHandled()?.let { track ->
                openPlayer(track)
            }
        }

        viewModel.showNoLastTrackMessage.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(
                    this,
                    getString(R.string.no_last_track),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun openPlayer(track: Track) {
        startActivity(MediaActivity.createIntent(this, track))
    }
}