package com.spacex.presentation.falcons

import com.spacex.model.FalconInfo


sealed interface FalconsEvent {
    data object LoadFavorites : FalconsEvent
    data object ToggleFindActive : FalconsEvent
    data class ToggleFavorites(val falconInfo: FalconInfo) : FalconsEvent
    data class NavigateToFalconInfoDetails(val falconInfo: FalconInfo) : FalconsEvent
    data object ClearError : FalconsEvent
}
