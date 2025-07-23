package com.spacex.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacex.model.FalconInfo
import com.spacex.model.mapToDomain
import com.spacex.repository.FalconRepository
import com.spacex.utils.AppPreferences
import com.spacex.utils.UiText
import kmpspacex.composeapp.generated.resources.Res
import kmpspacex.composeapp.generated.resources.error_failed_to_load_favorites
import kmpspacex.composeapp.generated.resources.success_removed_from_favorites
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoriteViewModel(
    val repository: FalconRepository,
    val appPreferences: AppPreferences
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<FavoritesState>(
            FavoritesState(isLoading = true)
        )

    val uiState = _uiState
        .onStart {
            handleEvent(FavoritesEvent.LoadFavorites)
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

    private val _effect = MutableSharedFlow<FavoritesEffect>()
    val effect: SharedFlow<FavoritesEffect> = _effect.asSharedFlow()

    fun handleEvent(event: FavoritesEvent) {
        when (event) {
            FavoritesEvent.LoadFavorites -> loadFavorites()
            FavoritesEvent.ClearError -> clearError()
            is FavoritesEvent.NavigateToFalconInfoDetails -> navigateToDetails(event.falconInfo)
            is FavoritesEvent.ToggleFavorites -> toggleFavorite(event.falconInfo)
        }
    }

    private fun toggleFavorite(falconInfo: FalconInfo) {
        viewModelScope.launch {
            if (falconInfo.isBookMark) {
                repository.unBookmark(falconInfo.id)
            } else {
                repository.bookmark(falconInfo.id)
            }
            _effect.emit(
                FavoritesEffect.ShowSuccess(
                    UiText.StringResource(Res.string.success_removed_from_favorites, falconInfo.name)
                )
            )
        }
    }

    private fun navigateToDetails(falconInfo: FalconInfo) {
        viewModelScope.launch {
            _effect.emit(FavoritesEffect.NavigateToDetails(falconInfo))
        }
    }

    private fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun removeFromFavorites(falconInfo: FalconInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.unBookmark(falconInfo.id)
        }
    }


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

    private fun loadFavorites() {
        viewModelScope.launch {
            repository.loadFavoriteData()
                .catch { error ->
                    _uiState.update { it ->
                        it.copy(
                            isLoading = false,
                            error =
                                error.message?.let { message ->
                                    UiText.StaticString(message)
                                }
                                    ?: UiText.StringResource(Res.string.error_failed_to_load_favorites)
                        )
                    }
                }
                .collect { falconEntities ->
                    _uiState.update {
                        _uiState.value.copy(
                            favoritesFalconInfos =
                                if (falconEntities.isEmpty()) {
                                    emptyList()
                                } else {
                                    falconEntities.map {
                                        it.mapToDomain()
                                    }
                                },
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}