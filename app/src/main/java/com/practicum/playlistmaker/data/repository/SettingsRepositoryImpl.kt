package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.storage.SettingsStorage
import com.practicum.playlistmaker.domain.models.ThemeSettings
import com.practicum.playlistmaker.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val storage: SettingsStorage
) : SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(
            storage.getDarkTheme()
        )
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        storage.saveDarkTheme(settings.darkTheme)
    }
}