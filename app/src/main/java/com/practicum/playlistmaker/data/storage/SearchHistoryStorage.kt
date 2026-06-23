package com.practicum.playlistmaker.data.storage

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.dto.TrackStorageDto

class SearchHistoryStorage(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(
        SEARCH_HISTORY_PREFERENCES,
        Context.MODE_PRIVATE
    )

    private val gson = Gson()

    fun getHistory(): List<TrackStorageDto> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)

        return if (json != null) {
            gson.fromJson(json, object : TypeToken<List<TrackStorageDto>>() {}.type)
        } else {
            emptyList()
        }
    }

    fun addTrack(track: TrackStorageDto) {
        val history = getHistory().toMutableList()

        history.removeAll { it.trackId == track.trackId }
        history.add(0, track)

        if (history.size > MAX_HISTORY_SIZE) {
            history.removeAt(history.lastIndex)
        }

        saveHistory(history)
    }

    fun clearHistory() {
        sharedPreferences.edit()
            .remove(SEARCH_HISTORY_KEY)
            .apply()
    }

    private fun saveHistory(history: List<TrackStorageDto>) {
        val json = gson.toJson(history)

        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }

    companion object {
        private const val SEARCH_HISTORY_PREFERENCES = "search_history_preferences"
        private const val SEARCH_HISTORY_KEY = "search_history_key"
        private const val MAX_HISTORY_SIZE = 10
    }
}