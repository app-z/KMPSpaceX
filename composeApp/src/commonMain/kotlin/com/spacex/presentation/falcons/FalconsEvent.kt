package com.spacex.presentation.falcons

import com.spacex.model.FalconInfo


sealed interface FalconsEvent {
    data object LoadFalcons : FalconsEvent
    data class LoadFilteredFalcons(val filter: String) : FalconsEvent
    data object ToggleFindActive : FalconsEvent
    data class ToggleFavorites(val falconInfo: FalconInfo) : FalconsEvent
    data class NavigateToFalconInfoDetails(val falconInfo: FalconInfo) : FalconsEvent
    data object ClearError : FalconsEvent
}
