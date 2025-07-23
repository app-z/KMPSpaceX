package com.spacex.viewmodel

import com.spacex.model.FalconInfo

class FalconsContract {

    sealed class Event : ViewEvent {
        data object Retry : Event()
        data class FalconSelection(val rocket: FalconInfo) : Event()
    }

    data class State(
        val rockets: List<FalconInfo>,
        val isLoading: Boolean,
        val isError: Boolean,
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        object DataWasLoaded : Effect()

        sealed class Navigation : Effect() {
            data class ToRocketInfo(val falconId: String): Navigation()
        }
    }

}
