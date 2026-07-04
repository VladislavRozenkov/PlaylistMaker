package com.practicum.playlistmaker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.domain.interactor.GetThemeSettingsInteractor
import com.practicum.playlistmaker.domain.interactor.UpdateThemeSettingsInteractor

class SettingsViewModelFactory(
    private val getThemeSettingsInteractor: GetThemeSettingsInteractor,
    private val updateThemeSettingsInteractor: UpdateThemeSettingsInteractor
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(
                getThemeSettingsInteractor,
                updateThemeSettingsInteractor
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}