package com.practicum.playlistmaker.settings.domain.interactor

import com.practicum.playlistmaker.settings.domain.model.ThemeSettings
import com.practicum.playlistmaker.settings.domain.repository.SettingsRepository

class UpdateThemeSettingsInteractorImpl(
    private val repository: SettingsRepository
) : UpdateThemeSettingsInteractor {
    override fun execute(settings: ThemeSettings) {
        repository.updateThemeSetting(settings)
    }
}