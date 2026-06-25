package com.practicum.playlistmaker.domain.interactor

import com.practicum.playlistmaker.domain.models.ThemeSettings
import com.practicum.playlistmaker.domain.repository.SettingsRepository

class UpdateThemeSettingsInteractorImpl(
    private val repository: SettingsRepository
) : UpdateThemeSettingsInteractor {
    override fun execute(settings: ThemeSettings) {
        repository.updateThemeSetting(settings)
    }
}