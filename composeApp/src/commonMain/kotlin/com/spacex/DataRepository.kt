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
package com.spacex

import com.spacex.database.SpaceXDao
import com.spacex.model.FalconEntity
import com.spacex.model.mapToEntity
import com.spacex.network.SpaceXApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DataRepository(
    private val api: SpaceXApi,
    private var dao: SpaceXDao,
//    private val cartDataStore: CartDataStore,
    private val scope: CoroutineScope,
) {




    fun getData(): Flow<List<FalconEntity>> {
        scope.launch {
            if (dao.count() < 1) {
                refreshData()
            }
        }
        return loadData()
    }

    fun loadData(): Flow<List<FalconEntity>> = dao.getAllRockets()

    suspend fun refreshData() {
        val response = api.getData()
        val rocketsEntry = response.map { it.mapToEntity() }
        dao.insertAllRockets(rocketsEntry)
    }
}
