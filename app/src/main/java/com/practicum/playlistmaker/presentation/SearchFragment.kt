package com.practicum.playlistmaker.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    companion object {
        private const val DEFAULT_SEARCH_QUERY = ""
        private const val SEARCH_QUERY_KEY = "SEARCH_QUERY"
    }

    private var _binding: FragmentSearchBinding? = null

    private val binding: FragmentSearchBinding
        get() = _binding!!

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    private val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(
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

        setupRecyclerViews()
        setupListeners()
        observeViewModel()

        viewModel.onScreenOpened()

        restoreSearchQuery(savedInstanceState)

        binding.searchEditText.post {
            binding.searchEditText.requestFocus()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        _binding?.let { currentBinding ->
            outState.putString(
                SEARCH_QUERY_KEY,
                currentBinding.searchEditText.text.toString()
            )
        }

        super.onSaveInstanceState(outState)
    }

    private fun restoreSearchQuery(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            return
        }

        val restoredText = savedInstanceState.getString(
            SEARCH_QUERY_KEY,
            DEFAULT_SEARCH_QUERY
        )

        binding.searchEditText.setText(restoredText)
        binding.searchEditText.setSelection(restoredText.length)
    }

    private fun setupRecyclerViews() {
        trackAdapter = TrackAdapter(ArrayList())
        historyAdapter = TrackAdapter(ArrayList())

        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        binding.recyclerView.adapter = trackAdapter

        binding.historyRecycler.layoutManager =
            LinearLayoutManager(requireContext())

        binding.historyRecycler.adapter = historyAdapter
    }

    private fun setupListeners() {
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
                ) {

                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int, count: Int
                ) {

                }

                override fun afterTextChanged(text: Editable?) {
                    val searchText = text?.toString().orEmpty()

                    binding.clearIcon.visibility =
                        if (searchText.isEmpty()) {
                            View.GONE
                        } else {
                            View.VISIBLE
                        }

                    viewModel.onSearchTextChanged(
                        searchText,
                        binding.searchEditText.hasFocus()
                    )
                }
            }
        )
        
        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()

                viewModel.searchNow(
                    binding.searchEditText.text.toString()
                )
                true
            } else {
                false
            }
        }
    }

    private fun observeViewModel() {
        viewModel.screenState.observe(viewLifecycleOwner) { state ->
            render(state)
        }

        viewModel.navEvents.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { track ->
                openPlayer(track)
            }
        }
    }

    private fun render(state: SearchScreenState) {
        when (state) {
            is SearchScreenState.EmptyInput -> {
                showEmptyInput()
            }

            is SearchScreenState.Loading -> {
                showLoading()
            }

            is SearchScreenState.Content -> {
                showContent(state.tracks)
            }

            is SearchScreenState.EmptyResult -> {
                showEmptyResult()
            }

            is SearchScreenState.Error -> {
                showError()
            }

            is SearchScreenState.History -> {
                showHistory(state.tracks)
            }
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

        binding.stateImage.setImageResource(
            R.drawable.sad_smiley_face
        )

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

        binding.stateImage.setImageResource(
            R.drawable.there_is_no_internet_connection
        )

        binding.stateTitle.text =
            getString(R.string.communication_problems)

        binding.stateMessage.text =
            getString(R.string.communication_problems2)

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
        val intent = MediaActivity.createIntent(
            requireContext(),
            track
        )

        startActivity(intent)
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager

        binding.searchEditText.windowToken?.let { token ->
            inputMethodManager.hideSoftInputFromWindow(
                token,
                0
            )
        }
    }

    override fun onDestroyView() {
        binding.recyclerView.adapter = null
        binding.historyRecycler.adapter = null

        _binding = null

        super.onDestroyView()
    }
}