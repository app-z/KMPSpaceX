package com.spacex.model

import com.spacex.utils.AppPreferences.Companion.CARD_MODE
import com.spacex.utils.NetworkResponse

data class FalconUiState(
    val networkResponse : NetworkResponse<List<FalconInfo>>,
    val rowMode: String = CARD_MODE
)
