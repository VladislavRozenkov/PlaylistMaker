package com.practicum.playlistmaker



import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

class SearchActivity : AppCompatActivity() {

    companion object {
        const val DEFAULT_SEARCH_QUERY = ""
        const val SEARCH_QUERY_KEY = "SEARCH_QUERY"
    }
    private lateinit var binding: ActivitySearchBinding
    private lateinit var trackAdapter: TrackAdapter

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ItunesApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivitySearchBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        trackAdapter = TrackAdapter(ArrayList())
        recyclerView.adapter = trackAdapter

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
                binding.clearIcon.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

        }

        binding.searchEditText.addTextChangedListener(simpleTextWatcher)

        binding.clearIcon.setOnClickListener {
            binding.searchEditText.text.clear()
            hideKeyboard()
        }

        binding.stateButton.setOnClickListener {
            val query = binding.searchEditText.text.toString().trim()

            if (query.isNotEmpty()) {
                hideKeyboard()
                searchTracks(query)
            }
        }

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
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

        showLoading()

        api.search(query).enqueue(object : retrofit2.Callback<ItunesResponse> {

            override fun onResponse(
                call: Call<ItunesResponse>,
                response: Response<ItunesResponse>) {

                if (response.isSuccessful) {
                    val tracks = response.body()?.results ?: emptyList()

                    val mappedTracks = tracks.map {
                        Track(
                            it.trackName,
                            it.artistName,
                            formatTime(it.trackTimeMillis),
                            it.artworkUrl100
                        )
                    }

                    if (mappedTracks.isEmpty()) {
                        showEmpty()
                    } else {
                        trackAdapter.updateTracks(mappedTracks)
                        showContent()
                    }

                } else {
                    showError()
                }

            }

            override fun onFailure(call: Call<ItunesResponse>, t: Throwable) {
                showError()
            }
        })
    }

    private fun formatTime(millis: Long): String {
        return java.text.SimpleDateFormat("mm:ss", java.util.Locale.getDefault()).format(millis)
    }

    private fun showLoading() {
        binding.recyclerView.visibility = View.GONE
        binding.stateView.visibility = View.GONE
    }

    private fun showContent() {
        binding.recyclerView.visibility = View.VISIBLE
        binding.stateView.visibility = View.GONE
    }

    private fun showEmpty() {
        binding.recyclerView.visibility = View.GONE
        binding.stateView.visibility = View.VISIBLE

        binding.stateImage.setImageResource(R.drawable.sad_smiley_face)
        binding.stateTitle.text = getString(R.string.nothing)
        binding.stateMessage.text = ""
        binding.stateButton.visibility = View.GONE
    }

    private fun showError() {
        binding.recyclerView.visibility = View.GONE
        binding.stateView.visibility = View.VISIBLE

        binding.stateImage.setImageResource(R.drawable.there_is_no_internet_connection)
        binding.stateTitle.text = getString(R.string.communication_problems)
        binding.stateMessage.text = getString(R.string.communication_problems2)
        binding.stateButton.visibility = View.VISIBLE
    }
}