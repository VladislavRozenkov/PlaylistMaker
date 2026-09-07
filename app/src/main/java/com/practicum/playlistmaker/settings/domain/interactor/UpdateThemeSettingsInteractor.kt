package com.practicum.playlistmaker.settings.domain.interactor

import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

interface UpdateThemeSettingsInteractor {
    fun execute(settings: ThemeSettings)
}