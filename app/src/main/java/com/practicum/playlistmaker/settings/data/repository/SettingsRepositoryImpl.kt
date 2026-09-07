package com.practicum.playlistmaker.settings.data.repository

import com.practicum.playlistmaker.settings.domain.model.ThemeSettings
import com.practicum.playlistmaker.settings.domain.repository.SettingsRepository
import com.practicum.playlistmaker.settings.data.storage.SettingsStorage

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