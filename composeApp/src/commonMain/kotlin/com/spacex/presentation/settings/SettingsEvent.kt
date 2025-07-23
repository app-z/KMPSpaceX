package com.spacex.presentation.settings

sealed interface SettingsEvent {
    data class Theme(val currentThene: String) : SettingsEvent
    data class RowMode(val mode: String) : SettingsEvent
}
