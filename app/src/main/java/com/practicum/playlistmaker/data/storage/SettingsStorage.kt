package com.practicum.playlistmaker.data.storage

import android.content.Context

class SettingsStorage(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(
        SETTINGS_PREFERENCES,
        Context.MODE_PRIVATE
    )

    fun getDarkTheme(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME_KEY, false)
    }

    fun saveDarkTheme(isDarkTheme: Boolean) {
        sharedPreferences.edit()
            .putBoolean(DARK_THEME_KEY, isDarkTheme)
            .apply()
    }

    companion object {
        private const val SETTINGS_PREFERENCES = "settings_preferences"
        private const val DARK_THEME_KEY = "dark_theme_key"
    }
}