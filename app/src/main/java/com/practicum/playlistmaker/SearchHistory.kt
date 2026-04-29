package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val prefs: SharedPreferences) {

    private val gson = Gson()

    fun getHistory(): MutableList<Track> {
        val json = prefs.getString(KEY_HISTORY, null)?: return mutableListOf()
        val type = object : TypeToken<MutableList<Track>>() {}.type
        return gson.fromJson(json, type)
    }

    fun addTrack(track: Track) {
        val history = getHistory()
        history.removeAll { it.trackId == track.trackId }
        history.add(0, track)
        if (history.size > MAX_SIZE)
            history.removeAt(history.lastIndex)
        save(history)
    }

    fun clear() {
        prefs.edit()
            .remove(KEY_HISTORY)
            .apply()
    }

    fun save(list: List<Track>) {
        val json = gson.toJson(list)
        prefs.edit()
            .putString(KEY_HISTORY, json)
            .apply()
    }

    companion object {
        private const val KEY_HISTORY = "search_history"
        private const val MAX_SIZE = 10
    }
}