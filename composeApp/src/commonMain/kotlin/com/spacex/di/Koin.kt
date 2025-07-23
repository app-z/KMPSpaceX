package com.spacex.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            databaseModule,
            networkModule,
            repositoryModule,
            viewmodelModule
        )
    }