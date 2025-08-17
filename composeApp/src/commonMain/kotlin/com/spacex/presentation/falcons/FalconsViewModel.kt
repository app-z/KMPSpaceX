package com.spacex.presentation.falcons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacex.model.FalconInfo
import com.spacex.model.mapToDomain
import com.spacex.model.mapToEntity
import com.spacex.presentation.favorite.FavoriteViewModel
import com.spacex.repository.FalconRepository
import com.spacex.repository.OnlineFalconRepository
import com.spacex.utils.AppPreferences
import com.spacex.utils.UiText
import kmpspacex.composeapp.generated.resources.Res
import kmpspacex.composeapp.generated.resources.success_removed_from_favorites
import kmpspacex.composeapp.generated.resources.success_toggle_favorites
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FalconsViewModel(
    val repository: FalconRepository,
    val onlineRepository: OnlineFalconRepository,
    val appPreferences: AppPreferences,
    val falconRepository: FalconRepository
) : IFalconsViewModel<FalconsState>, ViewModel() {

    private val _uiState = MutableStateFlow(
        FalconsState(isLoading = true)
    )

    override val uiState = _uiState
        .onStart {
            handleEvent(FalconsEvent.LoadFalcons)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = _uiState.value
        )


    val rowMode: StateFlow<String> =
        appPreferences.getRowModeFlow()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Companion.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AppPreferences.Companion.CARD_MODE,
            )

    init {
        viewModelScope.launch {
            rowMode.collect {
                updateCurrentRowMode(it)
            }
        }
    }

    private val _effect = MutableSharedFlow<FalconsEffect>()
    val effect: SharedFlow<FalconsEffect> = _effect.asSharedFlow()

    fun handleEvent(event: FalconsEvent) {
        when (event) {
            FalconsEvent.ToggleFindActive -> toggleFindActiveFlag()
            FalconsEvent.LoadFalcons -> loadFalcons()
            FalconsEvent.ClearError -> clearError()
            is FalconsEvent.NavigateToFalconInfoDetails -> navigateToDetails(event.falconInfo)
            is FalconsEvent.ToggleFavorites -> toggleFavorite(event.falconInfo)
            is FalconsEvent.LoadFilteredFalcons -> getFilteredFalcons(event.filter)
        }
    }

    private fun toggleFindActiveFlag() {
        val isFindActiveCurrent = uiState.value.isFindActive
        _uiState.update {
            it.copy(isFindActive = !isFindActiveCurrent)
        }
    }

    private fun navigateToDetails(falconInfo: FalconInfo) {
        viewModelScope.launch {
            _effect.emit(FalconsEffect.NavigateToDetails(falconInfo = falconInfo))
        }
    }

    private fun clearError() {
        _uiState.update {
            it.copy(error = null)
        }
    }

    fun updateCurrentRowMode(mode: String) {
        _uiState.update {
            it.copy(
                rowMode = mode
            )
        }
    }

    private fun loadFalcons() {
        viewModelScope.launch {

            repository.loadData().collect { falconEntities ->
                if (falconEntities.isEmpty()) {

                    showLoaderState()


                    val res = onlineRepository.getData(0)
                    if (res.isSuccess) {
                        res.map { rocketsResults ->
                            withContext(Dispatchers.IO) {
                                repository.insertFalcons(rocketsResults.map {
                                    it.mapToEntity()
                                })
                            }
                            _uiState.update {
                                it.copy(
                                    falconInfos = rocketsResults.map { it.mapToDomain() },
                                    isLoading = false,
                                    error = null
                                )
                            }
                        }
                    } else {
                        if (res.isFailure) {
                            _uiState.emit(
                                uiState.value.copy(
                                    error = UiText.StaticString(
                                        res.exceptionOrNull()?.message ?: "Error throw"
                                    ),
                                    isLoading = false,
                                )
                            )
                        } else {
                            _uiState.emit(
                                uiState.value.copy(
                                    error = UiText.StaticString("Unknown network error"),
                                    isLoading = false,
                                )
                            )
                        }

                    }
                } else {
                    _uiState.update {
                        it.copy(
                            falconInfos = falconEntities.map { it.mapToDomain() },
                            isLoading = false,
                            error = null
                        )
                    }
                }
            }
        }
    }


    private fun getFilteredFalcons(filter: String) {
        viewModelScope.launch {

//            showLoaderState()
            if (filter.isEmpty()) {
                loadFalcons()
            } else {

                repository.loadFilteredData(filter = filter)
                    .collect { falconEntities ->
                        _uiState.update { state ->
                            state.copy(
                                falconInfos = if (falconEntities.isEmpty()) {
                                    emptyList()
                                } else {
                                    falconEntities.map {
                                        it.mapToDomain()
                                    }
                                },
                                error = null,
                                isLoading = false
                            )
                        }
                    }
            }
        }
    }

    private suspend fun showLoaderState() {
        _uiState.emit(
            uiState.value.copy(isLoading = true)
        )
    }

    private fun toggleFavorite(falconInfo: FalconInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!falconInfo.isBookMark) {
                falconRepository.bookmark(falconInfo.id)
            } else {
                falconRepository.unBookmark(falconInfo.id)
            }
            _effect.emit(
                FalconsEffect.ShowSuccess(
                    UiText.StringResource(Res.string.success_toggle_favorites, falconInfo.name)
                )
            )
        }
    }

    companion object Companion {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}
