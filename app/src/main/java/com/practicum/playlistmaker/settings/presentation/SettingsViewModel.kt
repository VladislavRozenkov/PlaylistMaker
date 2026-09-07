package com.practicum.playlistmaker.settings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.core.presentation.Event
import com.practicum.playlistmaker.settings.domain.interactor.GetThemeSettingsInteractor
import com.practicum.playlistmaker.settings.domain.interactor.UpdateThemeSettingsInteractor
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

class SettingsViewModel(
    private val getThemeSettingsInteractor: GetThemeSettingsInteractor,
    private val updateThemeSettingsInteractor: UpdateThemeSettingsInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<SettingsScreenState>()
    val screenState: LiveData<SettingsScreenState> = _screenState

    private val _themeChanged = MutableLiveData<Event<Boolean>>()
    val themeChanged: LiveData<Event<Boolean>> = _themeChanged

    fun onScreenOpened() {
        val themeSettings = getThemeSettingsInteractor.execute()

        _screenState.value = SettingsScreenState(
            themeSettings.darkTheme
        )
    }

    fun onThemeSwitchChanged(isChecked: Boolean) {
        updateThemeSettingsInteractor.execute(
            ThemeSettings(isChecked)
        )

        _screenState.value = SettingsScreenState(isChecked)

        _themeChanged.value = Event(isChecked)
    }
}