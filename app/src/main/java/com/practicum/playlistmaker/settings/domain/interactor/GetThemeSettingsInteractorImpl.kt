package com.practicum.playlistmaker.settings.domain.interactor

import com.practicum.playlistmaker.settings.domain.model.ThemeSettings
import com.practicum.playlistmaker.settings.domain.repository.SettingsRepository

class GetThemeSettingsInteractorImpl(
    private val repository: SettingsRepository
) : GetThemeSettingsInteractor {
    override fun execute(): ThemeSettings {
        return repository.getThemeSettings()
    }
}