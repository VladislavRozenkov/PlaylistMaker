package com.practicum.playlistmaker


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {

    companion object {
        const val DEFAULT_SEARCH_QUERY = ""
        const val SEARCH_QUERY_KEY = "SEARCH_QUERY"
    }

    private var searchQuery: String = DEFAULT_SEARCH_QUERY

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_search)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val clearIcon = findViewById<ImageView>(R.id.clearIcon)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        val simpleTextWatcher = object : TextWatcher {
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
                before: Int,
                count: Int
            ) {

            }

            override fun afterTextChanged(s: Editable?) {
                searchQuery = s?.toString() ?: DEFAULT_SEARCH_QUERY
                clearIcon.visibility = clearButtonVisibility(s)
            }

        }

        searchEditText.addTextChangedListener(simpleTextWatcher)

        clearIcon.setOnClickListener {
            searchEditText.text.clear()
            hideKeyboard()
        }

        if (savedInstanceState != null) {
            val restoredText = savedInstanceState.getString(SEARCH_QUERY_KEY, DEFAULT_SEARCH_QUERY)
            searchEditText.setText(restoredText)
            searchEditText.setSelection(restoredText.length)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("SEARCH_QUERY_KEY", searchQuery)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.windowToken?.let { token ->
            imm.hideSoftInputFromWindow(token, 0)
        }
    }
}