package com.practicum.playlistmaker.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.interactor.GetThemeSettingsInteractor
import com.practicum.playlistmaker.domain.interactor.UpdateThemeSettingsInteractor
import com.practicum.playlistmaker.domain.models.ThemeSettings

class SettingsViewModel(
    private val getThemeSettingsInteractor: GetThemeSettingsInteractor,
    private val updateThemeSettingsInteractor: UpdateThemeSettingsInteractor
) : ViewModel() {

    private val screenStateMutable = MutableLiveData<SettingsScreenState>()
    val screenState: LiveData<SettingsScreenState> = screenStateMutable

    private val themeChangedMutable = MutableLiveData<Event<Boolean>>()
    val themeChanged: LiveData<Event<Boolean>> = themeChangedMutable

    fun onScreenOpened() {
        val themeSettings = getThemeSettingsInteractor.execute()

        screenStateMutable.value = SettingsScreenState(
            themeSettings.darkTheme
        )
    }

    fun onThemeSwitchChanged(isChecked: Boolean) {
        updateThemeSettingsInteractor.execute(
            ThemeSettings(isChecked)
        )

        screenStateMutable.value = SettingsScreenState(isChecked)

        themeChangedMutable.value = Event(isChecked)
    }
}