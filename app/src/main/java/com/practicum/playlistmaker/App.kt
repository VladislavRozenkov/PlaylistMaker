package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    private lateinit var sharedPrefs: SharedPreferences

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        sharedPrefs = getSharedPreferences("settings", MODE_PRIVATE)

        darkTheme = sharedPrefs.getBoolean("dark_theme", darkTheme)

        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {

        darkTheme = darkThemeEnabled

        sharedPrefs.edit()
            .putBoolean("dark_theme", darkThemeEnabled)
            .apply()

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}