package com.spacex.di

import com.spacex.viewmodel.FalconDetailViewModel
import com.spacex.viewmodel.FavoriteViewModel
import com.spacex.viewmodel.MainViewModel
import com.spacex.viewmodel.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewmodelModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::FalconDetailViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::FavoriteViewModel)
}