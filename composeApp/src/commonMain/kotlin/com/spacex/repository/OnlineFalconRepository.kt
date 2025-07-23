package com.spacex.repository

import com.spacex.model.RocketsResult
import com.spacex.utils.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlin.coroutines.cancellation.CancellationException

class OnlineFalconRepository(
    private val networkModule: HttpClient
) {

    suspend fun getData(pageNumber: Int): Result<List<RocketsResult>> {
        return try {
            val rockets: Array<RocketsResult> =
                networkModule.get(BASE_URL).body()
            Result.success(rockets.asList())
        } catch (e: Exception) {
            if (e is CancellationException) {
                e.printStackTrace()
                Result.success(emptyList<RocketsResult>())
            } else
                Result.failure(exception = e)
        }
    }
}
