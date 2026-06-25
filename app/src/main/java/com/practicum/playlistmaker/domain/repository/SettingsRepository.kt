package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.models.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}