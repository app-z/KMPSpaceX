/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.spacex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacex.model.FalconUiState
import com.spacex.model.mapToDomain
import com.spacex.repository.FalconRepository
import com.spacex.utils.AppPreferences
import com.spacex.utils.AppPreferences.Companion.CARD_MODE
import com.spacex.utils.NetworkResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoriteViewModel(
    val repository: FalconRepository,
    val appPreferences: AppPreferences
) : IFalconsViewModel<FalconUiState>, ViewModel() {

    private val _uiState =
        MutableStateFlow<FalconUiState>(
            FalconUiState(networkResponse = NetworkResponse.Loading)
        )

    override val uiState = _uiState.asStateFlow()

    val rowMode: StateFlow<String> =
        appPreferences.getRowModeFlow()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CARD_MODE,
            )


    init {
        viewModelScope.launch {
            rowMode.collect {
                updateCurrentRowMode(it)
            }
        }
    }

    private fun updateCurrentRowMode(mode: String) {
        _uiState.update {
            _uiState.value.copy(
                rowMode = mode
            )
        }
    }

    override fun getFalcons() {
        viewModelScope.launch {

            repository.loadFavoriteData().collect { falconEntities ->
                _uiState.update {
                    _uiState.value.copy(
                        if (falconEntities.isEmpty()) {
                            NetworkResponse.Empty
                        } else {
                            NetworkResponse.Success(
                                falconEntities.map {
                                    it.mapToDomain()
                                }
                            )
                        }
                    )
                }
            }
        }
    }

    private suspend fun showLoaderState() {
        _uiState.emit(
            _uiState.value.copy(networkResponse = NetworkResponse.Loading)
        )
    }

    override fun getFilteredFalcons(filter: String) {
        viewModelScope.launch {

            showLoaderState()

            repository.loadFilteredFavoriteData(filter = filter)
                .collect { falconEntities ->
                    _uiState.emit(
                        _uiState.value.copy(
                            if (falconEntities.isEmpty()) {
                                NetworkResponse.Empty
                            } else {
                                NetworkResponse.Success(
                                    falconEntities.map {
                                        it.mapToDomain()
                                    }
                                )
                            }
                        )
                    )
                }
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}
