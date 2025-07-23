package com.spacex.viewmodel

import kotlinx.coroutines.flow.StateFlow

interface IFalconsViewModel<T> {

    val uiState: StateFlow<T>

    fun getFalcons()

    fun getFilteredFalcons(filter: String)
}