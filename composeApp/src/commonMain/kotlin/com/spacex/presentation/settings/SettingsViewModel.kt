package com.spacex.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacex.utils.AppPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SettingsViewModel(
    private val appPreferences: AppPreferences,
) : ViewModel() {

    private val _state: MutableStateFlow<SettingsState> = MutableStateFlow(
        SettingsState()
    )
    val state = _state.asStateFlow()

    init {
        getCurrentRowMode()
        currentThemeGet()
    }

    fun handleEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.RowMode -> setRowModeState(event.mode)
            is SettingsEvent.Theme -> changeThemeMode(event.currentThene)
        }
    }

    private fun currentThemeGet() = runBlocking {
        _state.emit(
            state.value.copy(currentTheme = appPreferences.getTheme())
        )
    }

    private fun changeThemeMode(mode: String) = viewModelScope.launch(Dispatchers.IO) {
        appPreferences.changeThemeMode(mode)
        _state.update {
            it.copy(currentTheme = mode)
        }
    }

    private fun getCurrentRowMode() = runBlocking {
        _state.update {
            it.copy(rowMode = appPreferences.getRowMode())
        }
    }

    private fun setRowModeState(mode: String) {
        viewModelScope.launch {
            appPreferences.setRowMode(mode)
            _state.update {
                it.copy(rowMode = mode)

            }
        }
    }

}