package com.spacex.utils

sealed class NetworkResponse<out T> {
    data object Idle : NetworkResponse<Nothing>()
    data object Loading : NetworkResponse<Nothing>()
    data object Empty : NetworkResponse<Nothing>()
    data class Success<T>(val data: T) : NetworkResponse<T>()
    data class Error(val message: String) : NetworkResponse<Nothing>()
}
