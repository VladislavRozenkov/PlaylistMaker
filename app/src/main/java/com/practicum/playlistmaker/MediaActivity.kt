package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.databinding.ActivityMediaBinding

class MediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        renderTrack()
    }

    private fun renderTrack() {
        val trackName = intent.getStringExtra(EXTRA_TRACK_NAME).orEmpty()
        val artistName = intent.getStringExtra(EXTRA_ARTIST_NAME).orEmpty()
        val trackTime = intent.getStringExtra(EXTRA_TRACK_TIME).orEmpty()
        val artworkUrl100 = intent.getStringExtra(EXTRA_ARTWORK_URL_100).orEmpty()
        val collectionName = intent.getStringExtra(EXTRA_COLLECTION_NAME).orEmpty()
        val releaseDate = intent.getStringExtra(EXTRA_RELEASE_DATE).orEmpty()
        val primaryGenreName = intent.getStringExtra(EXTRA_PRIMARY_GENRE_NAME).orEmpty()
        val country = intent.getStringExtra(EXTRA_COUNTRY).orEmpty()

        binding.trackName.text = trackName
        binding.executor.text = artistName
        binding.time.text = "00:00"
        binding.durationValue.text = trackTime
        setOptionalField(
            binding.albumTitle,
            binding.albumValue,
            collectionName
        )
        setOptionalField(
            binding.yearTitle,
            binding.yearValue,
            releaseDate.take(4)
        )
        binding.genreValue.text = primaryGenreName
        binding.countryValue.text = country

        Glide.with(this)
            .load(getCoverArtwork(artworkUrl100))
            .placeholder(R.drawable.snake)
            .error(R.drawable.snake)
            .into(binding.imageCover)
    }

    private fun getCoverArtwork(artworkUrl100: String): String {
        return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    }

    private fun setOptionalField(titleView: TextView, valueView: TextView, value: String) {

        if (value.isEmpty()) {
            titleView.visibility = View.GONE
            valueView.visibility = View.GONE
        } else {
            titleView.visibility = View.VISIBLE
            valueView.visibility = View.VISIBLE
            valueView.text = value
        }
    }

    companion object {
        private const val EXTRA_TRACK_NAME = "trackName"
        private const val EXTRA_ARTIST_NAME = "artistName"
        private const val EXTRA_TRACK_TIME = "trackTime"
        private const val EXTRA_ARTWORK_URL_100 = "artworkUrl100"
        private const val EXTRA_COLLECTION_NAME = "collectionName"
        private const val EXTRA_RELEASE_DATE = "releaseDate"
        private const val EXTRA_PRIMARY_GENRE_NAME = "primaryGenreName"
        private const val EXTRA_COUNTRY = "country"

        fun createIntent(context: Context, track: Track): Intent {
            return Intent(context, MediaActivity::class.java).apply {
                putExtra(EXTRA_TRACK_NAME, track.trackName)
                putExtra(EXTRA_ARTIST_NAME, track.artistName)
                putExtra(EXTRA_TRACK_TIME, track.trackTime)
                putExtra(EXTRA_ARTWORK_URL_100, track.artworkUrl100)
                putExtra(EXTRA_COLLECTION_NAME, track.collectionName)
                putExtra(EXTRA_RELEASE_DATE, track.releaseDate)
                putExtra(EXTRA_PRIMARY_GENRE_NAME, track.primaryGenreName)
                putExtra(EXTRA_COUNTRY, track.country)
            }
        }
    }
}