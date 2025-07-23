package com.spacex.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.spacex.database.AppDatabase
import com.spacex.datastore.dataStorePreferences
import com.spacex.utils.AppPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module
import com.spacex.utils.getDatabaseBuilder

val databaseModule = module {

    // database
    single {
        getRoomDatabase(getDatabaseBuilder())
    }

    // datastore
    single {
        AppPreferences(dataStorePreferences())
    }


}


fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

