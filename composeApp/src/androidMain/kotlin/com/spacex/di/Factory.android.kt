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

import android.app.Application
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.spacex.database.AppDatabase
import com.spacex.database.DB_FILE_NAME
import com.spacex.network.SpaceXApi
import kotlinx.coroutines.Dispatchers
import org.koin.mp.KoinPlatform

actual class Factory() {
    actual fun createRoomDatabase(): AppDatabase {
        val appContext = KoinPlatform.getKoin().get<Application>()
        val dbFile = appContext.getDatabasePath(DB_FILE_NAME)
        return Room
            .databaseBuilder<AppDatabase>(
                context = appContext,
                name = dbFile.absolutePath,
            ).setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    actual fun createApi(): SpaceXApi = commonCreateApi()
}
