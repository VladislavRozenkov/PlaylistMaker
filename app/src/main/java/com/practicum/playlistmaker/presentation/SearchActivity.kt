package com.practicum.playlistmaker.presentation

import android.os.Bundle
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
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    companion object {
        const val DEFAULT_SEARCH_QUERY = ""
        const val SEARCH_QUERY_KEY = "SEARCH_QUERY"
    }

    private lateinit var binding: ActivitySearchBinding
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private val viewModel: SearchViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupInsets()
        setupRecyclerViews()
        setupListeners()
        observeViewModel()

        binding.searchEditText.post {
            binding.searchEditText.requestFocus()
        }

        viewModel.onScreenOpened()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY_KEY, binding.searchEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val restoredText = savedInstanceState.getString(
            SEARCH_QUERY_KEY,
            DEFAULT_SEARCH_QUERY
        )

        binding.searchEditText.setText(restoredText)
        binding.searchEditText.setSelection(restoredText.length)
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

    private fun setupRecyclerViews() {
        trackAdapter = TrackAdapter(ArrayList())
        historyAdapter = TrackAdapter(ArrayList())

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = trackAdapter

        binding.historyRecycler.layoutManager = LinearLayoutManager(this)
        binding.historyRecycler.adapter = historyAdapter
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        trackAdapter.onClick = { track ->
            viewModel.onTrackClicked(track)
        }

        historyAdapter.onClick = { track ->
            viewModel.onTrackClicked(track)
        }

        binding.clearHistoryBtn.setOnClickListener {
            viewModel.clearHistory()
        }

        binding.clearIcon.setOnClickListener {
            binding.searchEditText.text.clear()
            hideKeyboard()
        }

        binding.stateButton.setOnClickListener {
            hideKeyboard()
            viewModel.retrySearch()
        }

        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            viewModel.onSearchFocusChanged(hasFocus)
        }

        binding.searchEditText.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {}

                override fun afterTextChanged(s: Editable?) {
                    val text = s?.toString().orEmpty()

                    binding.clearIcon.visibility =
                        if (text.isEmpty()) View.GONE else View.VISIBLE

                    viewModel.onSearchTextChanged(
                        text,
                        binding.searchEditText.hasFocus()
                    )
                }
            }
        )

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                viewModel.searchNow(binding.searchEditText.text.toString())
                true
            } else {
                false
            }
        }
    }

    private fun observeViewModel() {
        viewModel.screenState.observe(this) { state ->
            render(state)
        }

        viewModel.navEvents.observe(this) { event ->
            event.getContentIfNotHandled()?.let { track ->
                openPlayer(track)
            }
        }
    }

    private fun render(state: SearchScreenState) {
        when (state) {
            is SearchScreenState.EmptyInput -> showEmptyInput()
            is SearchScreenState.Loading -> showLoading()
            is SearchScreenState.Content -> showContent(state.tracks)
            is SearchScreenState.EmptyResult -> showEmptyResult()
            is SearchScreenState.Error -> showError()
            is SearchScreenState.History -> showHistory(state.tracks)
        }
    }

    private fun showEmptyInput() {
        trackAdapter.updateTracks(emptyList())
        historyAdapter.updateTracks(emptyList())

        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.stateView.visibility = View.GONE
        binding.historyContainer.visibility = View.GONE
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.stateView.visibility = View.GONE
        binding.historyContainer.visibility = View.GONE
    }

    private fun showContent(tracks: List<Track>) {
        trackAdapter.updateTracks(tracks)

        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.stateView.visibility = View.GONE
        binding.historyContainer.visibility = View.GONE
    }

    private fun showEmptyResult() {
        trackAdapter.updateTracks(emptyList())

        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.stateView.visibility = View.VISIBLE
        binding.historyContainer.visibility = View.GONE

        binding.stateImage.setImageResource(R.drawable.sad_smiley_face)
        binding.stateTitle.text = getString(R.string.nothing)
        binding.stateMessage.text = ""
        binding.stateButton.visibility = View.GONE
    }

    private fun showError() {
        trackAdapter.updateTracks(emptyList())

        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.stateView.visibility = View.VISIBLE
        binding.historyContainer.visibility = View.GONE

        binding.stateImage.setImageResource(R.drawable.there_is_no_internet_connection)
        binding.stateTitle.text = getString(R.string.communication_problems)
        binding.stateMessage.text = getString(R.string.communication_problems2)
        binding.stateButton.visibility = View.VISIBLE
    }

    private fun showHistory(tracks: List<Track>) {
        historyAdapter.updateTracks(tracks)

        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.stateView.visibility = View.GONE
        binding.historyContainer.visibility = View.VISIBLE
    }

    private fun openPlayer(track: Track) {
        startActivity(MediaActivity.createIntent(this, track))
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        binding.searchEditText.windowToken?.let { token ->
            imm.hideSoftInputFromWindow(token, 0)
        }
    }

}