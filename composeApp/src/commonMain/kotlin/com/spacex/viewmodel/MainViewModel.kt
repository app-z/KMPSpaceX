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
import com.spacex.entity.mapToDomain
import com.spacex.entity.mapToEntity
import com.spacex.model.FalconInfo
import com.spacex.model.RocketsResult
import com.spacex.repository.FalconRepository
import com.spacex.repository.OnlineFalconRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest

class MainViewModel(
    val repository: FalconRepository,
    val onlineRepository: OnlineFalconRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<HomeUiState> =
        repository
            .loadData()
            .transformLatest {
                emit(
                    if (it.isEmpty()) {
                        HomeUiState(
                            getOnlineFalcons()
                                .map {
                                    rocketsResult -> rocketsResult.mapToDomain()
                                }
                        )
                    } else {
                        HomeUiState(
                            falconInfo = it.map { falconEntity ->
                                falconEntity.mapToDomain()
                            }
                        )
                    }
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState(),
            )


    suspend fun getOnlineFalcons(): List<RocketsResult> {
        val res = onlineRepository.getData(0)
        if (res.isSuccess) {
            res.map { rocketsResults ->
                repository.insertFalcons(rocketsResults.map {
                    it.mapToEntity()
                })
                return rocketsResults
            }
        }
        return emptyList()
    }

}

    /**
     * Ui State for the home screen
     */
    data class HomeUiState(
        val falconInfo: List<FalconInfo> = listOf(),
    )

    private const val TIMEOUT_MILLIS = 5_000L
