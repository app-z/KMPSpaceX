package com.spacex.di

import com.spacex.database.AppDatabase
import com.spacex.repository.FalconRepository
import com.spacex.repository.OnlineFalconRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        FalconRepository(get<AppDatabase>().falconDao())
    }

    single {
        OnlineFalconRepository(get())
    }
}