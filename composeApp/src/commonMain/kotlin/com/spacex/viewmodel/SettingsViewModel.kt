package com.spacex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacex.utils.AppPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SettingsViewModel(
    private val appPreferences: AppPreferences,
) : ViewModel() {

    private val _currentTheme: MutableStateFlow<String?> = MutableStateFlow(null)
    val currentTheme = _currentTheme.asStateFlow()


    private val _modeState = MutableStateFlow<String?>(null)
    val modeState = _modeState.asStateFlow()

    init {
        getCurrentRowMode()
        currentThemeGet()
    }

    private fun currentThemeGet() = runBlocking {
        _currentTheme.value = appPreferences.getTheme()
    }

    fun changeThemeMode(value: String) = viewModelScope.launch(Dispatchers.IO) {
        appPreferences.changeThemeMode(value)
        _currentTheme.value = value
    }

    private fun getCurrentRowMode() = runBlocking {
        _modeState.value = appPreferences.getRowMode()
    }

    fun setRowModeState(mode: String) {
        viewModelScope.launch {
            appPreferences.setRowMode(mode)
            _modeState.value = mode
        }
    }

}


//data class SettingsState (val rowModeCard: Boolean = true)