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
import com.spacex.model.mapToDomain
import com.spacex.model.mapToEntity
import com.spacex.model.FalconInfo
import com.spacex.model.RocketsResult
import com.spacex.repository.FalconRepository
import com.spacex.repository.OnlineFalconRepository
import com.spacex.utils.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    val repository: FalconRepository,
    val onlineRepository: OnlineFalconRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<NetworkResponse<List<FalconInfo>>>(NetworkResponse.Loading)
    val uiState = _uiState.asStateFlow()

    fun getFalcons() {
        viewModelScope.launch {

            _uiState.emit(NetworkResponse.Loading)

            repository.loadData().collect { falconEntities ->
                if (falconEntities.isEmpty()) {
                    val res = onlineRepository.getData(0)
                    if (res.isSuccess) {
                        res.map { rocketsResults ->
                            withContext(Dispatchers.IO) {
                                repository.insertFalcons(rocketsResults.map {
                                    it.mapToEntity()
                                })
                            }
                            _uiState.update {
                                NetworkResponse.Success(rocketsResults.map { it.mapToDomain() })
                            }
                        }
                    } else {
                        if (res.isFailure) {
                            _uiState.emit(
                                NetworkResponse.Error(
                                    res.exceptionOrNull()?.message ?: "Error throw"
                                )
                            )
                        } else {
                            _uiState.emit(NetworkResponse.Error("Unknown network error"))
                        }

                    }
                } else {
                    _uiState.update {
                        NetworkResponse.Success((falconEntities.map { it.mapToDomain() }))
                    }
                }
            }
        }
    }

    init {
        getFalcons()
    }

//    @OptIn(ExperimentalCoroutinesApi::class)
//    val uiState: StateFlow<HomeUiState> =
//        repository
//            .loadData()
//            .transformLatest {
//                emit(
//                    if (it.isEmpty()) {
//                        val onlineRes = getOnlineFalcons()
//                        if (onlineRes.isEmpty()) {
//                            HomeUiState(error = ERROR_LOADING_DATA)
//                        } else {
//                            HomeUiState(
//                                getOnlineFalcons()
//                                    .map { rocketsResult ->
//                                        rocketsResult.mapToDomain()
//                                    }
//                            )
//                        }
//                    } else {
//                        HomeUiState(
//                            falconInfo = it.map { falconEntity ->
//                                falconEntity.mapToDomain()
//                            }
//                        )
//                    }
//                )
//            }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//                initialValue = HomeUiState(),
//            )

    suspend fun getOnlineFalcons(): List<RocketsResult> {
        return withContext(Dispatchers.IO) {
            val res = onlineRepository.getData(0)
            if (res.isSuccess) {
                res.map { rocketsResults ->
                    repository.insertFalcons(rocketsResults.map {
                        it.mapToEntity()
                    })
                    rocketsResults
                }
            }
            emptyList()
        }
    }

}

/**
 * Ui State for the home screen
 */
data class HomeUiState(
    val falconInfo: List<FalconInfo> = listOf(),
    val error: Int = 0
)

private const val TIMEOUT_MILLIS = 5_000L
