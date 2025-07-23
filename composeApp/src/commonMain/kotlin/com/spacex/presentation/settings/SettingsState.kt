package com.spacex.presentation.settings

import com.spacex.utils.AppPreferences.Companion.CARD_MODE

data class SettingsState(
    val currentTheme: String = com.spacex.utils.Theme.DARK_MODE.name,
    val rowMode: String = CARD_MODE
)
