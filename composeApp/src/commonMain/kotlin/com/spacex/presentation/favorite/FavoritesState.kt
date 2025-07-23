package com.spacex.presentation.favorite

import com.spacex.model.FalconInfo
import com.spacex.utils.AppPreferences.Companion.CARD_MODE
import com.spacex.utils.UiText

data class FavoritesState(
    val favoritesFalconInfos: List<FalconInfo> = emptyList(),
    val isLoading: Boolean = false,
    val rowMode: String = CARD_MODE,
    val error: UiText? = null,
)