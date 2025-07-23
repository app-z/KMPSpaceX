package com.spacex.presentation.falcons


import com.spacex.model.FalconInfo
import com.spacex.utils.UiText

sealed interface FalconsEffect {
    data class NavigateToDetails(val falconInfo: FalconInfo) : FalconsEffect
    data class ShowError(val message: UiText) : FalconsEffect
    data class ShowSuccess(val message: UiText) : FalconsEffect
}