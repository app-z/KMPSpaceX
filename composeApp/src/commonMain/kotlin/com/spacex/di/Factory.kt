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
package com.spacex.di

import com.spacex.database.AppDatabase
import com.spacex.network.SpaceXApi
import com.spacex.network.SpaceXNetworkApi
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

expect class Factory {
    fun createRoomDatabase(): AppDatabase

    fun createApi(): SpaceXApi
//
//    fun createCartDataStore(): CartDataStore
}

internal fun commonCreateApi(): SpaceXApi =
    SpaceXNetworkApi(
        client = HttpClient {
            install(ContentNegotiation) {
                json(json, contentType = ContentType.Any)
            }
        },
//        apiUrl = "https://api.spacexdata.com/v4/launches"
    )

val json = Json { ignoreUnknownKeys = true }
