package com.practicum.playlistmaker.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.data.network.ItunesApi
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.SearchHistory
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.di.Creator
import com.practicum.playlistmaker.domain.interactor.SearchTracksInteractor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale

class SearchActivity : AppCompatActivity() {

    companion object {
        const val DEFAULT_SEARCH_QUERY = ""
        const val SEARCH_QUERY_KEY = "SEARCH_QUERY"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchHistory: SearchHistory
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private var lastSearchQuery = ""
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {
        val query = binding.searchEditText.text.toString().trim()
        if (query.isNotEmpty()) {
            searchTracks(query)
        }
    }

    private val searchTracksInteractor: SearchTracksInteractor by lazy {
        Creator.provideSearchTracksInteractor()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivitySearchBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.searchEditText.post {
            binding.searchEditText.requestFocus()
        }

        val prefs = getSharedPreferences("search_prefs", MODE_PRIVATE)
        searchHistory = SearchHistory(prefs)
        val historyList = searchHistory.getHistory()

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        trackAdapter = TrackAdapter(ArrayList())
        recyclerView.adapter = trackAdapter

        val historyRecycler = binding.historyRecycler
        historyRecycler.layoutManager = LinearLayoutManager(this)
        historyAdapter = TrackAdapter(ArrayList())
        historyRecycler.adapter = historyAdapter
        historyAdapter.updateTracks(historyList)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val text = s?.toString().orEmpty()

                binding.clearIcon.visibility =
                    if (text.isEmpty()) View.GONE else View.VISIBLE

                if (binding.searchEditText.hasFocus()
                    && text.isEmpty()
                    && searchHistory.getHistory().isNotEmpty()
                    ) {
                    binding.historyContainer.visibility = View.VISIBLE
                } else {
                    binding.historyContainer.visibility = View.GONE
                }

                if (text.trim().isEmpty()) {
                    handler.removeCallbacks(searchRunnable)
                    lastSearchQuery = ""
                    trackAdapter.updateTracks(emptyList())
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                    binding.stateView.visibility = View.GONE
                } else {
                    searchDebounce()
                }
            }

        }

        binding.clearHistoryBtn.setOnClickListener {
            searchHistory.clear()
            historyAdapter.updateTracks(emptyList())
            binding.historyContainer.visibility = View.GONE
        }

        trackAdapter.onClick = { track ->
            if (clickDebounce()) {
                searchHistory.addTrack(track)

                val updatedHistory = searchHistory.getHistory()
                historyAdapter.updateTracks(updatedHistory)

                startActivity(MediaActivity.createIntent(this, track))
            }

        }

        historyAdapter.onClick = { track ->
            if (clickDebounce()) {
                searchHistory.addTrack(track)

                val updatedHistory = searchHistory.getHistory()
                historyAdapter.updateTracks(updatedHistory)

                startActivity(MediaActivity.createIntent(this, track))
            }

        }

        binding.searchEditText.setOnFocusChangeListener {_, hasFocus ->
            if (hasFocus && binding.searchEditText.text.isNullOrEmpty()
                && searchHistory.getHistory().isNotEmpty()) {
                binding.historyContainer.visibility = View.VISIBLE
            } else {
                binding.historyContainer.visibility = View.GONE
            }
        }

        binding.searchEditText.addTextChangedListener(simpleTextWatcher)

        binding.clearIcon.setOnClickListener {
            binding.searchEditText.text.clear()
            hideKeyboard()

            trackAdapter.updateTracks(emptyList())

            binding.recyclerView.visibility = View.GONE
            binding.stateView.visibility = View.GONE

            if (searchHistory.getHistory().isNotEmpty()) {
                binding.historyContainer.visibility = View.VISIBLE
            }
        }

        binding.stateButton.setOnClickListener {
            if (lastSearchQuery.isNotEmpty()) {
                hideKeyboard()
                searchTracks(lastSearchQuery)
            }
        }

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = binding.searchEditText.text.toString().trim()

                if (query.isNotEmpty()) {
                    hideKeyboard()
                    searchTracks(query)
                }

                true

            } else {

                false
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY_KEY, binding.searchEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val restoredText = savedInstanceState.getString(SEARCH_QUERY_KEY, DEFAULT_SEARCH_QUERY)
        binding.searchEditText.setText(restoredText)
        binding.searchEditText.setSelection(restoredText.length)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        binding.searchEditText.windowToken?.let { token ->
            imm.hideSoftInputFromWindow(token, 0)
        }
    }

    private fun searchTracks(query: String) {

        binding.historyContainer.visibility = View.GONE

        lastSearchQuery = query

        showLoading()

        searchTracksInteractor.execute(
            query,
            { tracks ->
                if (tracks.isEmpty()) {
                    showEmpty()
                } else {
                    trackAdapter.updateTracks(tracks)
                    showContent()
                }
            },
            {
                showError()
            }
        )
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.stateView.visibility = View.GONE
        binding.historyContainer.visibility = View.GONE
    }

    private fun showContent() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.stateView.visibility = View.GONE
    }

    private fun showEmpty() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.stateView.visibility = View.VISIBLE
        binding.stateImage.setImageResource(R.drawable.sad_smiley_face)
        binding.stateTitle.text = getString(R.string.nothing)
        binding.stateMessage.text = ""
        binding.stateButton.visibility = View.GONE
    }

    private fun showError() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.stateView.visibility = View.VISIBLE
        binding.stateImage.setImageResource(R.drawable.there_is_no_internet_connection)
        binding.stateTitle.text = getString(R.string.communication_problems)
        binding.stateMessage.text = getString(R.string.communication_problems2)
        binding.stateButton.visibility = View.VISIBLE
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed

        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }

        return current
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
    }
}