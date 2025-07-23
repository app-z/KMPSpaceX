package com.spacex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacex.utils.AppPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SettingsViewModel(
    private val appPreferences: AppPreferences,
) : ViewModel() {

    private val _modeState = MutableStateFlow<String?>(null)
    val modeState = _modeState.asStateFlow()

    init {
        getCurrentRowMode()
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