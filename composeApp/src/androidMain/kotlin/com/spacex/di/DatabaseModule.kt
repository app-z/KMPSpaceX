package com.spacex.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.spacex.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val databaseModule = module {

    // database
    single {
        Factory().createRoomDatabase()
    }

//    // datastore
//    single {
//        AppPreferences(dataStorePreferences())
//    }

}
