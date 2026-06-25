package com.practicum.playlistmaker.domain.interactor

import com.practicum.playlistmaker.domain.models.ThemeSettings

interface UpdateThemeSettingsInteractor {
    fun execute(settings: ThemeSettings)
}