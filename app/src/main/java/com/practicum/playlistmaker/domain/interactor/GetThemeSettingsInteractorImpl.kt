package com.practicum.playlistmaker.domain.interactor

import com.practicum.playlistmaker.domain.models.ThemeSettings
import com.practicum.playlistmaker.domain.repository.SettingsRepository

class GetThemeSettingsInteractorImpl(
    private val repository: SettingsRepository
) : GetThemeSettingsInteractor {
    override fun execute(): ThemeSettings {
        return repository.getThemeSettings()
    }
}