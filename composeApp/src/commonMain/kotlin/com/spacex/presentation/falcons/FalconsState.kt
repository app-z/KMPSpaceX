package com.spacex.presentation.falcons

import com.spacex.model.FalconInfo
import com.spacex.utils.AppPreferences.Companion.CARD_MODE
import com.spacex.utils.UiText

data class FalconsState(
    val falconInfos: List<FalconInfo> = emptyList(),
    val isLoading: Boolean = false,
    val isFindActive: Boolean = false,
    val rowMode: String = CARD_MODE,
    val error: UiText? = null,
)