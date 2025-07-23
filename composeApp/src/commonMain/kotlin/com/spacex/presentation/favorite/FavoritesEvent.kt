package com.spacex.presentation.favorite

import com.spacex.model.FalconInfo
import com.spacex.presentation.falcons.FalconsEvent


sealed interface FavoritesEvent {
    data object LoadFavorites : FavoritesEvent
    data class ToggleFavorites(val falconInfo: FalconInfo) : FavoritesEvent
    data class NavigateToFalconInfoDetails(val falconInfo: FalconInfo) : FavoritesEvent
    data object ClearError : FavoritesEvent
}
