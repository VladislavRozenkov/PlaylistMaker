package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    private lateinit var sharedPrefs: SharedPreferences

    var darkTheme = false
        private set

    override fun onCreate() {
        super.onCreate()

        sharedPrefs = getSharedPreferences("settings", MODE_PRIVATE)

        darkTheme = sharedPrefs.getBoolean(KEY_DARK_THEME, darkTheme)

        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {

        darkTheme = darkThemeEnabled

        sharedPrefs.edit()
            .putBoolean(KEY_DARK_THEME, darkThemeEnabled)
            .apply()

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        private const val KEY_DARK_THEME = "dark_theme"
    }
}