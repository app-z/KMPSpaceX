package com.spacex.presentation.favorite


import com.spacex.model.FalconInfo
import com.spacex.utils.UiText

sealed interface FavoritesEffect {
    data class NavigateToDetails(val falconInfo: FalconInfo) : FavoritesEffect
    data class ShowError(val message: UiText) : FavoritesEffect
    data class ShowSuccess(val message: UiText) : FavoritesEffect

    data object Initial : FavoritesEffect
}