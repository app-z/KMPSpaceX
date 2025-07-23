package com.spacex.presentation.falcons

import kotlinx.coroutines.flow.StateFlow

interface IFalconsViewModel<T> {

    val uiState: StateFlow<T>

    fun loadFalcons()

    fun getFilteredFalcons(filter: String)
}