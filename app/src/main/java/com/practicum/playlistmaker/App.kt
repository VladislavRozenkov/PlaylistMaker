package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.di.Creator

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val getThemeSettingsInteractor =
            Creator.provideGetThemeSettingsInteractor(applicationContext)

        switchTheme(getThemeSettingsInteractor.execute().darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}